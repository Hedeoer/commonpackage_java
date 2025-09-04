package message;

import com.google.gson.*;
import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.drive.v1.model.*;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 功能：上传文件到飞书云空间，并发送包含云空间文件的消息，之后@群成员
 * 1.上传的文件会显示在用户的云空间中，且文件不会过期
 * 2.只要文件存在（未被删除），就可被访问
 *
 */
public class PermanentFileMessage {

    private static final Logger logger = LoggerFactory.getLogger(PermanentFileMessage.class);
    // userAccessToken2小时过期，需要替换成自己的用户的User Access Token
    private static String userAccessToken = "u-didXeKUr5f4afur36Dod3z417ZHA5hyhogw040M00BxW";
    // 应用的App ID和App Secret
    private static String appId = "cli_a7daaadd86fb501c";
    private static String appSecret = "86azVQyRZlPOBR54OW6pbcTTwnIpsoRe";
    // 云空间目录ID，需要替换成自己的云空间目录ID
    private static String  parentNodeCode = "Ve56f5anTlMIKNdiOsYcN0cnn8d";
    // 接收消息的群ID，需要替换成自己的群ID
    private static String receiveId = "oc_5277e7cf7cee0f8012eecdbcc65120a2";
    // 被@的用户ID，需要替换成自己的用户ID,该用户必须在receiveId的群里
    private static String acceptUserId = "ou_16fbf5cb8e9cb3b0f2ccedfd774fe735";

    public static void sendPermanentFileMessage(String fileName) throws Exception {

        /*
        * 上传文件到云空间
        * 获取文件的共享链接
        * 发送包含云空间文件的消息
        * */

        // 构建client，需要替换成自己的应用的App ID和App Secret
        Client client = Client.newBuilder(appId, appSecret).build();

        // ===========================上传文件到云空间


        // 创建请求对象 parentNode需要替换成自己的云空间目录ID
        File file = new File(fileName);
        UploadAllFileReq req = UploadAllFileReq.newBuilder()
                .uploadAllFileReqBody(UploadAllFileReqBody.newBuilder()
                        .fileName(fileName)
                        .parentType("explorer")
                        .parentNode(parentNodeCode)
                        .size((int) file.length())
                        .file(file)
                        .build())
                .build();

        // 发起请求,userAccessToken2小时过期，需要替换成自己的用户的User Access Token
        UploadAllFileResp resp = client.drive().v1().file().uploadAll(req, RequestOptions.newBuilder()
                .userAccessToken(userAccessToken)
                .build());

        // 处理服务端错误
        if(!resp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return;
        }

        // 业务数据处理
        String fileToken = resp.getData().getFileToken();
        logger.info("文件上传成功，fileToken：{}", fileToken);

        // ===========================获取文件的共享链接
        String docToken = resp.getData().getFileToken();
        String docType = "file";
        String result = BatchQueryFileMeta(client, docToken,docType);
        String url = getFirstMetaUrlWithoutPojo(result);
        logger.info("获取文件的共享链接成功，文件链接：{}", url);


        // ===========================发送包含云空间文件的消息
        CreateMessageResp postResult = createPostMessage(client,url);
        // 非0表示失败
        int ifSuccess = postResult.getCode();
        logger.info("发送包含云空间文件的消息成功，结果：{}", ifSuccess);



    }



    private static CreateMessageResp createPostMessage(Client client,String fileLink) throws Exception {
        logger.info(fileLink);
        // 创建请求对象
        CreateMessageReq textReq = CreateMessageReq.newBuilder()
                .receiveIdType("chat_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId(receiveId)
                        .msgType("post")
                        .content("{\"zh_cn\":{\"title\":\"通知\",\"content\":[[{\"tag\":\"a\",\"href\":\""+fileLink+"\",\"text\":\"文件链接请查收\",\"style\":[\"bold\"]}],[{\"tag\":\"at\",\"user_id\":\""+acceptUserId+"\",\"style\":[\"italic\"]}]]}}")
                        .build())
                .build();

        // 发起请求
        CreateMessageResp textResp = client.im().v1().message().create(textReq);

        // 处理服务端错误
        if (!textResp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", textResp.getCode(), textResp.getMsg(), textResp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(textResp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return null;
        }

        return textResp;
    }

    private static String BatchQueryFileMeta(Client client, String docToken, String docType) throws Exception {

        // 创建请求对象
        BatchQueryMetaReq req = BatchQueryMetaReq.newBuilder()
                .metaRequest(MetaRequest.newBuilder()
                        .requestDocs(new RequestDoc[] {
                                RequestDoc.newBuilder()
                                        .docToken(docToken)
                                        .docType(docType)
                                        .build()
                        })
                        .withUrl(true)
                        .build())
                .build();

        // 发起请求
        BatchQueryMetaResp resp = client.drive().v1().meta().batchQuery(req, RequestOptions.newBuilder()
                .userAccessToken(userAccessToken)
                .build());

        // 处理服务端错误
        if(!resp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return null;
        }

        return Jsons.DEFAULT.toJson(resp.getData());
    }


    /**
     * 从 JSON 字符串中提取第一个 "metas" 数组元素的 "url" 字段值
     * @param jsonString 输入的 JSON 字符串
     * @return 如果成功，返回 "url" 字段的字符串值；否则返回 null
     */
    public static String getFirstMetaUrlWithoutPojo(String jsonString) {
        try {
            // 1. 使用 JsonParser 解析 JSON 字符串，得到一个通用的 JsonElement
            JsonElement jsonElement = JsonParser.parseString(jsonString);

            // 2. 确保它是 JsonObject 类型（最外层是一个对象）
            if (jsonElement.isJsonObject()) {
                JsonObject rootObject = jsonElement.getAsJsonObject();

                    // 4. 获取 "metas" 字段，并确保它是一个 JsonArray
                    if (rootObject.has("metas") && rootObject.get("metas").isJsonArray()) {
                        JsonArray metasArray = rootObject.getAsJsonArray("metas");

                        // 5. 检查 "metas" 数组是否为空
                        if (metasArray.size() > 0) {
                            // 6. 获取数组的第一个元素，并确保它是一个 JsonObject
                            JsonElement firstMetaElement = metasArray.get(0);
                            if (firstMetaElement.isJsonObject()) {
                                JsonObject firstMetaObject = firstMetaElement.getAsJsonObject();

                                // 7. 获取 "url" 字段的值，并确保它是一个基本类型（字符串、数字、布尔等）
                                if (firstMetaObject.has("url") && firstMetaObject.get("url").isJsonPrimitive()) {
                                    return firstMetaObject.get("url").getAsString();
                                }
                            }
                        }
                    }

            }
        } catch (JsonSyntaxException e) {
            logger.error("JSON 格式错误: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("解析 JSON 发生未知错误: {}", e.getMessage(), e);
        }
        return null; // 如果任何步骤失败，则返回 null
    }
}
