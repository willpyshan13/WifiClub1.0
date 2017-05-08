package cn.situne.itee.manager;

import java.util.Map;

/**
 * @author ZhiCheng Guo
 * @version 2014年10月7日 上午11:04:36
 */
public interface MultiPartRequest {

    void addFileUpload(String param, byte[] data);

    void addStringUpload(String param, String content);

    Map<String, byte[]> getFileUploads();

    Map<String, String> getStringUploads();
}