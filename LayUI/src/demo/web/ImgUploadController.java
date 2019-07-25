package demo.web;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import demo.demo.ImgResult;
/**
 * 单独文件上传
 * @author BLAME
 *
 */
@Controller
public class ImgUploadController {

	@RequestMapping("/uploadPic")
	@ResponseBody
	public ImgResult uploadPic(MultipartFile file, HttpServletRequest request) {
		String desFilePath = "";
		String oriName = "";
		ImgResult result = new ImgResult();
		Map<String, String> dataMap = new HashMap<>();
		ImgResult imgResult = new ImgResult();
		try {
			// 1.获取原文件名
			oriName = file.getOriginalFilename();
			// 2.获取原文件图片后缀，以最后的.作为截取(.jpg)
			String extName = oriName.substring(oriName.lastIndexOf("."));
			// 3.生成自定义的新文件名，这里以UUID.jpg|png|xxx作为格式（可选操作，也可以不自定义新文件名）
			String uuid =Long.toString(Calendar.getInstance().getTimeInMillis());
			String newName = uuid + extName;
			// 4.获取要保存的路径文件夹
			String realPath = request.getRealPath("statics/imgs/");
			// 5.保存
			desFilePath = realPath + "\\" + newName;
			File desFile = new File(desFilePath);
			file.transferTo(desFile);
			System.out.println(desFilePath);
			// 6.返回保存结果信息
			result.setCode(0);
			dataMap = new HashMap<>();
			dataMap.put("src", "statics/imgs/" + newName);
			result.setData(dataMap);
			result.setMsg(oriName + "上传成功！");
			return result;
		} catch (IllegalStateException e) {
			imgResult.setCode(1);
			return imgResult;
		} catch (IOException e) {
			e.printStackTrace();
			imgResult.setCode(1);
			return imgResult;
		}
	}
}
