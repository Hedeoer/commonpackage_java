package message;

import com.google.gson.JsonParser;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;

import java.io.File;
import java.nio.charset.StandardCharsets;
import com.lark.oapi.service.im.v1.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能：上传文件到飞书开放平台的媒体资源服务器中，并发送文件消息，之后@群成员
 * 使用飞书应用上传文件到飞书开放平台的媒体资源服务器中，不会直接显示在用户的云空间或云文档中，仅当通过发送消息接口将文件发送到会话后，接收者才能在聊天界面查看或下载该文件
 * 1.消息被撤回或删除后，对应的文件也将无法访问
 * 2.只要消息存在（未被撤回或删除），文件就可被访问
 * 3.仅消息所在会话的成员可查看和下载文件，且必须通过飞书客户端或 API 访问，无法直接通过 URL 访问原始文件地址
 */
public class TemporaryFileMessage {

    private static final Logger logger = LoggerFactory.getLogger(TemporaryFileMessage.class);

    public static void main(String arg[]) throws Exception {

        // 构建client
        Client client = Client.newBuilder("cli_a7daaadd86fb501c", "86azVQyRZlPOBR54OW6pbcTTwnIpsoRe").build();

        File file = new File("C:\\Users\\H\\Desktop\\aa.xlsx");

        // 创建请求对象
        CreateFileReq req = CreateFileReq.newBuilder()
                .createFileReqBody(CreateFileReqBody.newBuilder()
                        .fileType("xls")
                        .fileName("aa.xlsx")
                        .file(file)
                        .build())
                .build();

        // 发起请求
        CreateFileResp resp = client.im().v1().file().create(req);

        // 处理服务端错误
        if (!resp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", resp.getCode(), resp.getMsg(), resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return;
        }

        //=============================发送文件消息=========================
        // 业务数据处理
        String fileKey = resp.getData().getFileKey();
        logger.info("文件发送成功，fileKey：{}", fileKey);

        // 创建请求对象
        CreateMessageReq messageReq = CreateMessageReq.newBuilder()
                .receiveIdType("chat_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId("oc_5277e7cf7cee0f8012eecdbcc65120a2")
                        .msgType("file")
//                        .content("{\"zh_cn\":{\"title\":\"消息通知\",\"content\":[[{\"tag\":\"at\",\"user_id\":\"15aa3cef\",\"style\":[\"bold\"]}],[{\"tag\":\"media\",\"file_key\":\"" + fileKey + "\"}]]}}")
                        .content("{\"file_key\":\"" + fileKey + "\"}")
                        .build())
                .build();

        // 发起请求
        CreateMessageResp messageResp = client.im().v1().message().create(messageReq);

        // 处理服务端错误
        if (!messageResp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", messageResp.getCode(), messageResp.getMsg(), messageResp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(messageResp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return;
        }

        // 业务数据处理
        logger.info(Jsons.DEFAULT.toJson(messageResp.getData()));

        //=============================@群成员=========================

        // 创建请求对象
        CreateMessageReq textReq = CreateMessageReq.newBuilder()
                .receiveIdType("chat_id")
                .createMessageReqBody(CreateMessageReqBody.newBuilder()
                        .receiveId("oc_5277e7cf7cee0f8012eecdbcc65120a2")
                        .msgType("text")
                        .content("{\"text\":\"<at user_id=\\\"ou_16fbf5cb8e9cb3b0f2ccedfd774fe735\\\"></at> 文件请查收\"}")
                        .build())
                .build();

        // 发起请求
        CreateMessageResp textResp = client.im().v1().message().create(textReq);

        // 处理服务端错误
        if (!textResp.success()) {
            logger.error("code:{},msg:{},reqId:{}, resp:{}", textResp.getCode(), textResp.getMsg(), textResp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(new String(textResp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
            return;
        }

        // 业务数据处理
        logger.info(Jsons.DEFAULT.toJson(textResp.getData()));


    }
}
