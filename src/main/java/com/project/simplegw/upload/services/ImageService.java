package com.project.simplegw.upload.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.system.vos.Constants;
import com.project.simplegw.system.vos.ServiceMsg;
import com.project.simplegw.system.vos.ServiceResult;
import com.project.simplegw.upload.helpers.UploadPath;
import com.project.simplegw.upload.vos.UploadType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {
    
    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ImageService() {
        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }


    private void compressAndSave(MultipartFile mpFile, Path path, String filename) throws Exception {
        if(Files.notExists(path))
            Files.createDirectories(path);
    
        // Appending a trailing slash if needed(맨 끝에 붙는 슬래시를 trailing slash 이라고 한다. URL리소스가 디렉토리를 의미함.)
        String pathStr = path.toString();
        if(pathStr.charAt(pathStr.length() - 1) != File.separatorChar)
            pathStr += File.separator;

        File imgFile = new File(pathStr + filename);

        InputStream is = mpFile.getInputStream();
        OutputStream os = new FileOutputStream(imgFile);
        ImageOutputStream imgOutputStream = ImageIO.createImageOutputStream(os);

        BufferedImage image = ImageIO.read(is);

        ImageWriter imgWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(mpFile.getOriginalFilename())).next();
        ImageWriteParam writeParam = imgWriter.getDefaultWriteParam();

        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(Constants.IMAGE_UPLOAD_QUALITY);

        imgWriter.setOutput(imgOutputStream);
        imgWriter.write(null, new IIOImage(image, null, null), writeParam);

        is.close();
        os.flush();
        os.close();
        imgOutputStream.flush();
        imgOutputStream.close();
    }
    


    // ckeditor를 이용해 업로드할 경우 리턴값을 json형태로 보내줘야 하므로 HashMap으로 리턴한다.
    public HashMap<String, Object> upload(HttpServletRequest req, MultipartFile imgFile) {
        String imgName = imgFile.getOriginalFilename();
        String imgExtension = FilenameUtils.getExtension(imgName);

        // 중복방지를 위해 파일명 변경
        String conversionName = new StringBuilder(UUID.randomUUID().toString()).append(".").append(imgExtension).toString();

        // 업로드 후 이미지 불러오는 url을 리턴해줘야 한다.
        String imgLoadUrl = new StringBuilder(req.getContextPath()).append(Constants.IMAGE_GET_URL).append(UploadPath.daily()).append(conversionName.toString()).toString();

        // https://ckeditor.com/docs/ckeditor4/latest/guide/dev_file_upload.html
        HashMap<String, Object> result = new HashMap<>();
        HashMap<String, String> errorMsg = new HashMap<>();

        result.put("fileName", conversionName);
        result.put("url", imgLoadUrl);

        if(imgFile.getSize() > Constants.IMAGE_UPLOAD_MAX_SIZE) {
            errorMsg.put("message", "파일이 업로드 제한 용량을 초과하여 업로드 실패하였습니다.");
            result.put("uploaded", 0);
            result.put("error", errorMsg);

            return result;
        }

        if(!imgExtension.matches(Constants.IMAGE_EXTENSION_PATTERNS)) {
            errorMsg.put("message", new StringBuilder("이미지 파일이 아닙니다.").append("허용 확장자: ").append(Constants.IMAGE_EXTENSIONS).toString());
            result.put("uploaded", 0);
            result.put("error", errorMsg);

            return result;
        }

        try {
            Path imgDir = Paths.get(UploadPath.get(UploadType.IMAGE));
            compressAndSave(imgFile, imgDir, conversionName);   // 이미지 저장 시작

            result.put("uploaded", 1);   // ckeditor4에서 이미지 업로드 성공은 1, 실패는 0

        } catch(IOException e) {
            e.printStackTrace();
            log.warn("MultipartFile: {}", imgFile.toString());

            errorMsg.put("message", "파일 IO 에러 발생");
            result.put("uploaded", 0);
            result.put("error", errorMsg);

        } catch(IllegalStateException e) {
            e.printStackTrace();
            log.warn("MultipartFile: {}", imgFile.toString());

            errorMsg.put("message", "파일 상태 에러 발생");
            result.put("uploaded", 0);
            result.put("error", errorMsg);

        } catch(Exception e) {
            e.printStackTrace();
            log.warn("MultipartFile: {}", imgFile.toString());

            errorMsg.put("message", "이미지 업로드시 예외 발생");
            result.put("uploaded", 0);
            result.put("error", errorMsg);
        }
        
        return result;
    }



    public byte[] getImageByteStream(String pathYear, String pathMonth, String pathDay, String conversionName) {
        String imgPath = new StringBuilder(Constants.IMAGE_UPLOAD_PATH).append(pathYear).append(File.separator).append(pathMonth).append(File.separator).append(pathDay).append(File.separator).append(conversionName).toString();
        
        try (
            InputStream imgByteStream = new FileInputStream(imgPath);
        ) {
            return IOUtils.toByteArray(imgByteStream);

        } catch(FileNotFoundException e) {
            log.warn("getImageByteStream() FileNotFoundException.");
            log.warn("{}", e.getMessage());
            return null;

        } catch(IOException e) {
            log.warn("getImageByteStream() IOException.");
            log.warn("{}", e.getMessage());
            return null;
        }
    }




    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_IMG_USER_PORTRAIT, key = "#loginUser.getMember().getId()")
    public byte[] getPortrait(LoginUser loginUser) {
        log.info("Cacheable method 'getPortrait()' called. user: {}", loginUser.getMember().getId());

        String imgPath = new StringBuilder(Constants.MEMBER_PORTRAIT_PATH).append(loginUser.getMember().getId().toString()).append(Constants.PORTRAIT_IMAGE_EXTENSION).toString();

        try( InputStream imgByteStream = new FileInputStream(imgPath) ) {
            return IOUtils.toByteArray(imgByteStream);

        } catch(FileNotFoundException e) {
            log.warn("getPortrait() FileNotFoundException.");
            log.warn("{}", e.getMessage());
            log.warn("member: {}", loginUser.getMember().getId());
            return null;

        } catch(IOException e) {
            log.warn("getPortrait() IOException.");
            log.warn("{}", e.getMessage());
            log.warn("member: {}", loginUser.getMember().getId());
            return null;
        }
    }

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_IMG_USER_PORTRAIT, key = "#memberId")
    public byte[] getPortrait(Long memberId) {
        // 임직원 정보(employees profile)에서 캐싱한 데이터를 공통으로 사용하기 위해 추가한 메서드.
        String imgPath = new StringBuilder(Constants.MEMBER_PORTRAIT_PATH).append(memberId.toString()).append(Constants.PORTRAIT_IMAGE_EXTENSION).toString();

        try( InputStream imgByteStream = new FileInputStream(imgPath) ) {
            return IOUtils.toByteArray(imgByteStream);

        } catch(FileNotFoundException e) {
            // EmployeeService 클래스에서 컬렉션을 이용해 호출하므로 로그가 많이 발생하므로 주석.
            // 위에 LoginUser 파라미터를 받는 메서드에서 로그를 남기면 된다.

            // log.warn("getPortrait() FileNotFoundException.");
            // log.warn("{}", e.getMessage());
            // log.warn("member: {}", memberId);
            return null;

        } catch(IOException e) {
            // log.warn("getPortrait() IOException.");
            // log.warn("{}", e.getMessage());
            // log.warn("member: {}", memberId);
            return null;
        }
    }



    @CacheEvict(cacheManager = Constants.CACHE_MANAGER, cacheNames = Constants.CACHE_IMG_USER_PORTRAIT, key = "#loginUser.getMember().getId()")
    public ServiceMsg uploadPortrait(MultipartHttpServletRequest req, LoginUser loginUser) {
        log.info("CacheEvict method 'uploadPortrait()' called. user: {}", loginUser.getMember().getId());

        MultipartFile imgFile = req.getFile("img");
        
        if(imgFile == null)   // 파일이 없으면 종료
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);

        String memberId = loginUser.getMember().getId().toString();
        String imgExtension = Constants.PORTRAIT_IMAGE_EXTENSION;
        String conversionName = new StringBuilder(memberId).append(imgExtension).toString();
        
        if(imgFile.getSize() > Constants.IMAGE_UPLOAD_MAX_SIZE)
            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("파일이 업로드 제한 용량을 초과하여 업로드 실패하였습니다.");
        
        try {
            Path imgDir = Paths.get(Constants.MEMBER_PORTRAIT_PATH);
            compressAndSave(imgFile, imgDir, conversionName);
            
            return new ServiceMsg().setResult(ServiceResult.SUCCESS);
            
        } catch(Exception e) {
            e.printStackTrace();
            log.warn("uploadPortrait() exception.");
            log.warn("member: {}", loginUser.getMember().getId());

            return new ServiceMsg().setResult(ServiceResult.FAILURE).setMsg("사진 업로드 에러입니다. 관리자에게 문의하세요.");
        }
    }
}
