package utils;

import java.util.Set;

/**
 * 使用硬编码列表来校验图片文件后缀的工具类
 */
public final class ImageValidator {

    /**
     * 定义一个静态的、不可变的Set，用于存储所有我们认可的图片文件后缀。
     * 1. 使用 Set 而不是 List 或 Array，因为 Set 的 contains() 方法具有 O(1) 的平均时间复杂度，查询效率最高。
     * 2. 使用 static final，确保该集合在类加载时只被初始化一次，并且不可被修改，是线程安全的常量。
     * 3. 所有后缀都使用小写存储，以便进行不区分大小写的比较。
     */
    private static final Set<String> SUPPORTED_IMAGE_SUFFIXES;
    static {
        SUPPORTED_IMAGE_SUFFIXES = new java.util.HashSet<>();
        String[] suffixes = { "jpg", "jpeg", "png", "gif", "bmp", "wbmp", "ico", "svg", "heic", "heif", "webp" };
        for (String s : suffixes) {
            SUPPORTED_IMAGE_SUFFIXES.add(s);
        }
    }

    /**
     * 私有构造函数，防止该工具类被实例化。
     */
    private ImageValidator() {
    }

    /**
     * 通过硬编码的后缀列表，判断给定的文件名是否为图片格式。
     *
     * @param fileName 要检查的文件名，例如 "my_photo.JPG" 或 "document.pdf"。
     * @return 如果文件后缀在我们的硬编码列表中，则返回 true；否则返回 false。
     */
    public static boolean isImageByHardcodedList(String fileName) {
        // --- 1. 边缘情况处理 ---
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        // --- 2. 提取文件后缀 ---
        // 查找最后一个 '.' 的位置
        int dotIndex = fileName.lastIndexOf('.');

        // 如果没有找到 '.'，或者 '.' 是文件名的最后一个字符（例如 "myfile."），则认为没有有效的后缀。
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return false;
        }

        // 提取后缀名（点后面的部分），并立即转换为小写，以进行不区分大小写的比较。
        String suffix = fileName.substring(dotIndex + 1).toLowerCase();

        // --- 3. 在Set中进行高效查找 ---
        return SUPPORTED_IMAGE_SUFFIXES.contains(suffix);
    }

    /**
     * Main方法，用于测试 isImageByHardcodedList 方法的正确性。
     */
    public static void main(String[] args) {
        String[] testFiles = {
                "vacation.jpg",
                "logo.PNG",              // 测试大小写不敏感
                "company_logo.JPEG",
                "transparent_bg.gif",
                "user_avatar.webp",      // 测试较新的格式
                "archive.zip",           // 非图片
                "my.document.docx",      // 测试多点文件名
                "image_without_extension", // 无后缀
                "file_with_dot_at_end.", // 点在末尾
                "hiddenfile.bmp",        // Unix隐藏文件格式
                null,                    // 测试null输入
                ""                       // 测试空字符串输入
        };

        System.out.println("使用硬编码列表进行图片后缀校验：");
        for (String file : testFiles) {
            // 使用 String.format 格式化输出，更美观
            System.out.printf("文件: %-25s -> 是否是图片? %s%n", "'" + file + "'", isImageByHardcodedList(file));
        }
    }
}
