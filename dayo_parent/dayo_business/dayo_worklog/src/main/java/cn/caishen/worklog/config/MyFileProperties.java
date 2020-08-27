package cn.caishen.worklog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置，获取上传下载文件地址
 * @author LB
 */
@ConfigurationProperties(prefix = "myfile")
@Component
public class MyFileProperties {

    private String tempFilePath;
    private String myFolderPath;

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getMyFolderPath() {
        return myFolderPath;
    }

    public void setMyFolderPath(String myFolderPath) {
        this.myFolderPath = myFolderPath;
    }

    @Override
    public String toString() {
        return "MyFileProperties{" +
                "tempFilePath='" + tempFilePath + '\'' +
                ", myFolderPath='" + myFolderPath + '\'' +
                '}';
    }
}
