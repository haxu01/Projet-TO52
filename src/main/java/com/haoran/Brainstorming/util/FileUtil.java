package com.haoran.Brainstorming.util;

import com.haoran.Brainstorming.service.ISystemConfigService;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Objects;


@Component
public class FileUtil {

    private final Logger log = LoggerFactory.getLogger(FileUtil.class);

    @Resource
    private ISystemConfigService systemConfigService;

    public static String getFileMD5(InputStream in) {
        byte[] buffer = new byte[1024];
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            while (true) {
                int len;
                if ((len = in.read(buffer, 0, 1024)) == -1) {
                    in.close();
                    break;
                }

                digest.update(buffer, 0, len);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }

        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     *télécharger des fichiers
     * @param file
     * @param fileName
     * @param customPath
     * @return
     */
    public String upload(MultipartFile file, String fileName, String customPath) {
        try {
            if (file == null || file.isEmpty()) return null;

            if (StringUtils.isEmpty(fileName)) fileName = StringUtil.uuid();
            String suffix = "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            // Si le répertoire de stockage n'existe pas, le créer
            File savePath = new File(systemConfigService.selectAllConfig().get("upload_path").toString() + customPath);
            if (!savePath.exists()) savePath.mkdirs();

            // Épeler le nom du fichier et le suffixe sur le chemin téléchargé
            String localPath = systemConfigService.selectAllConfig().get("upload_path").toString() + customPath + "/" + fileName + suffix;
            File file1 = new File(localPath);
            if (file1.exists()) {
                file1.delete();
            }

            file.transferTo(file1);

            // Renvoie le chemin d'accès une fois le téléchargement réussi
            return systemConfigService.selectAllConfig().get("static_url") + customPath + "/" + fileName +
                    suffix + "?v=" + StringUtil.randomNumber(6);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param file
     * @param fileName
     * @param customPath
     * @return
     */
    public String ossUpload(MultipartFile file, String fileName, String customPath) {
        try {
            String baseUrl = systemConfigService.selectAllConfig().get("base_url");
            String ossKey = systemConfigService.selectAllConfig().get("oss_key");
            String ossSecret = systemConfigService.selectAllConfig().get("oss_secret");
            String ossBucket = systemConfigService.selectAllConfig().get("oss_bucket");
            String ossEndPoint = systemConfigService.selectAllConfig().get("oss_end_point");
            if (StringUtils.isEmpty(fileName)) fileName = StringUtil.uuid();
            String suffix = "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            fileName += suffix;
            OSSClient ossClient = new OSSClient(ossEndPoint, ossKey, ossSecret);
            ossClient.putObject(ossBucket, customPath + "/" + fileName, file.getInputStream());
            ossClient.shutdown();
            return baseUrl + "/common/show_img?name=" + customPath + "/" + fileName;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public String generatorOssUrl(String name) {
        String ossKey = systemConfigService.selectAllConfig().get("oss_key");
        String ossSecret = systemConfigService.selectAllConfig().get("oss_secret");
        String ossBucket = systemConfigService.selectAllConfig().get("oss_bucket");
        String ossEndPoint = systemConfigService.selectAllConfig().get("oss_end_point");
        Date expires = new Date(new Date().getTime() + 1000 * 60 * 60);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(ossBucket, name);
        generatePresignedUrlRequest.setExpiration(expires);
        OSSClient client = new OSSClient(ossEndPoint, ossKey, ossSecret);
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String qiniuUpload(MultipartFile file, String fileName, String customPath) {
        try {
            String qiniuKey = systemConfigService.selectAllConfig().get("qiniu_key");
            String qiniuSecret = systemConfigService.selectAllConfig().get("qiniu_secret");
            String qiniuBucket = systemConfigService.selectAllConfig().get("qiniu_bucket");
            String qiniuDomain = systemConfigService.selectAllConfig().get("qiniu_domain");
            if (StringUtils.isEmpty(fileName)) fileName = StringUtil.uuid();
            String suffix = "." + Objects.requireNonNull(file.getContentType()).split("/")[1];
            fileName += suffix;
            Configuration cfg = new Configuration(Region.region2());
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(qiniuKey, qiniuSecret);
            String key = fileName;
            String upToken = auth.uploadToken(qiniuBucket, key);
            Response response = uploadManager.put(file.getInputStream(), key, upToken, null, null);
            return qiniuDomain + "/" + fileName;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
