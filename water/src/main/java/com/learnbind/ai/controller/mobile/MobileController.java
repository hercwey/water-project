package com.learnbind.ai.controller.mobile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.learnbind.ai.bean.FastJsonUtils;
import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.bean.MeterBookBean;
import com.learnbind.ai.bean.MeterBookList;
import com.learnbind.ai.bean.MeterBookMeterBean;
import com.learnbind.ai.bean.MeterReadBean;
import com.learnbind.ai.bean.MeterReadResultBean;
import com.learnbind.ai.common.enumclass.EnumAppReadResult;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.MeterFactoryConstant;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.model.MeterRecordTempPhoto;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.user.UsersService;

import tk.mybatis.mapper.util.StringUtil;

/**
 * @author lenovo
 * 用于手机客户端登录及下载表册
 */

//@Controller
//@RequestMapping(value = "/api")

@RestController // 使用该注解后相当于每个控制器方法自动添加@ResponseBody注解
@RequestMapping("/api")
public class MobileController {
	private static Log log = LogFactory.getLog(MobileController.class);	
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private UsersService usersService;
	@Autowired
	private MeterBookService meterBookService;//表册服务
	@Autowired
	private MeterBookMeterService meterBookMeterService;//表册-表计关系服务
	@Autowired
	private CustomersService customersService;//客户服务
	@Autowired
	private MetersService metersService;//表计服务
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录表服务
	@Autowired
	private MeterRecordTempService meterRecordTempService;//抄表记录临时表服务
	@Autowired
	private MeterRecordTempPhotoService meterRecordTempPhotoService;//抄表记录临时照片表服务
	
	@RequestMapping(value = "/user/login")	
	public ResponseEntity<String> mobileLogin(String username, String password,HttpServletRequest request, Model model) {
		final String LOGIN_OK="1";      //登录正确常量
		final String LOGIN_ERROR="-1";  //登录错误常量
		
		//服务器端用户名及口令校验规则
		if(StringUtil.isEmpty(username)  || StringUtil.isEmpty(password)) {
			return new ResponseEntity<>(LOGIN_ERROR, HttpStatus.OK);
		}		
		
		log.debug("====================开始验证");		
		
		//数据库的口令是加密的口令.
		String encoderPassword = DigestUtils.md5Hex(password.toString()).toUpperCase();
		
		SysUsers user = new SysUsers();
		user.setUsername(username);
		user.setPassword(encoderPassword);
		//user.setPassword(password);
		
		List<SysUsers> userList = usersService.select(user);
		if(userList!=null && userList.size()==1){
			log.debug("====================验证正确");
			
			user = userList.get(0);
			HttpSession session = request.getSession();
			session.setAttribute(SessionConstant.USER, user);
			return new ResponseEntity<>(String.valueOf(user.getId()), HttpStatus.OK);
			
		}
		else {
			log.debug("====================验证错误");			
			//model.addAttribute("error_msg", "用户名或密码错误！");
			return new ResponseEntity<>(LOGIN_ERROR, HttpStatus.OK);
		}
		
	}
	
	/*
	 * 功能:
	 * 		APP端下载表册列表
	 * 参数:
	 * 		userId:登录用户ID
	 */
	@RequestMapping(value = "/meterBook/load")	
	public ResponseEntity<MeterBookList> meterBookLoad(Long userId,HttpServletRequest request, Model model) {
		log.debug("----------APP端下载 表册列表");
		//表册列表
		MeterBookList meterBookList=new MeterBookList();
		//根据抄表员ID查询表册
		List<MeterBook> mbList = meterBookService.getReaderBookListByCondition(userId, null);
		
		List<MeterBookBean> mbbList = new ArrayList<>();
		log.debug("----------开始打印表册信息----------");
		for(MeterBook mb : mbList) {
			MeterBookBean tempMeterBook=new MeterBookBean();
			//表册信息
			tempMeterBook.setMeterBookId(mb.getId());//表册ID
			tempMeterBook.setName(mb.getName());//表册名称 格式：小区-楼号-单元
			tempMeterBook.setEquipType(mb.getFactory());//厂家
			tempMeterBook.setReadMode(mb.getReadMode());//抄表方式(readMode)：BLUETOOTH=蓝牙抄表；MANUAL=人工抄表；REMOTE=远程抄表；
			//表册分类
			tempMeterBook.setTypeCode(mb.getTypeCode());//分类编码
			tempMeterBook.setTypeName(mb.getTypeName());//分类名称
			tempMeterBook.setBookUserType(mb.getBookUserType());//表册用户类型
			log.debug("----------"+tempMeterBook.getName()+"："+tempMeterBook.toString());
			mbbList.add(tempMeterBook);
		}
		log.debug("----------打印表册信息完成----------");
		
		meterBookList.setMeterBooks(mbbList);
		
		/** //生成测试数据
		MeterBookList meterBookList=new MeterBookList();		
		List<MeterBookBean> meterBooks= meterBookList.getMeterBooks();		
		for(int i=0;i<30;i++) {
			MeterBookBean tempMeterBook=new MeterBookBean();
			//表计信息
			tempMeterBook.setEquipType("jc");
			tempMeterBook.setMeterBookId(i+1);
			tempMeterBook.setName("长久花园-10-"+i);
			//表册分类
			tempMeterBook.setTypeCode("0"+i);
			tempMeterBook.setTypeName("长久花园"+i);
						
			meterBooks.add(tempMeterBook);
		} **/
		
		return new ResponseEntity<>(meterBookList, HttpStatus.OK);			
	}
	
	/*
	 * 功能:
	 * 		APP端下载"表册-表计"
	 * 参数:
	 * 		meterBookId: 表册ID 
	 */
	@RequestMapping(value = "/meterBookMeter/load")	
	public ResponseEntity<MeterBookMeterBean> meterBookMeterLoad(Long meterBookId,HttpServletRequest request, Model model) {
		log.debug("----------APP端下载 表册-表计");
		Date date = new Date();
		Long start = date.getTime();
		
		//根据表册ID查询表册
		MeterBook mb = meterBookService.selectByPrimaryKey(meterBookId);
		
		//封装后的MeterBean集合
		List<MeterBean> meterBeanList = new ArrayList<>();
		//根据表册ID查询表册-表计关系
		List<MeterBookMeter> mbmList = this.getMeterBookMeterList(meterBookId);
		log.debug("----------开始打印表计信息----------");
		for(MeterBookMeter mbm : mbmList) {
			Long customerId = mbm.getCustomerId();//客户ID
			Long meterId = mbm.getMeterId();//表计ID
			if(customerId!=null && meterId!=null) {
				Date date2 = new Date();
				Long start1 = date2.getTime();
				Customers customer = customersService.selectByPrimaryKey(customerId);
				Meters meter = metersService.selectByPrimaryKey(meterId);
				if(meter.getVirtualReal()==EnumMeterVirtualReal.VIRTUAL_METER.getValue()) {//如果表计为虚表时，则过滤不下载
					log.debug("----------表计编号-"+meter.getMeterNo()+"表计虚/实状态-"+EnumMeterVirtualReal.getName(meter.getVirtualReal())+",表计描述-"+meter.getDescription()+",表计安装位置-"+meter.getPlace()+",此表计虚表，不需要下载到表册中...");
					continue;
				}
				MeterRecord record = meterRecordService.getLastMeterRecord(customerId, null, meterId);
				Date date3 = new Date();
				Long end1 = date3.getTime();
				Long d1 = end1-start1;
				System.out.println("查询客户信息、表计信息和抄表记录用时： "+d1+" ms");
				String currRead = "0";//表底
				BigDecimal preAmount = new BigDecimal(0.00);//水量
				if(record!=null) {
					String read = record.getCurrRead();
					if(StringUtils.isNotBlank(read)) {
						currRead = read;
					}
					BigDecimal amount = record.getCurrAmount();
					if(amount!=null) {
						preAmount = amount;
					}
				}else {
					String read = meter.getNewMeterBottom();
					if(StringUtils.isNotBlank(read)) {
						currRead = read;
					}
				}
				
				//获取MeterBean
				MeterBean meterBean = this.getMeterBean(customer, meter, currRead, preAmount);
				log.debug("----------表计信息："+meterBean.getCustomer()+"："+meterBean.toString());
				meterBeanList.add(meterBean);
			}
		}
		log.debug("----------打印表计信息完成----------");
		
		//获取返回数据表册表计Bean
		MeterBookMeterBean meterBookMeter=this.getMeterBookMeterBean(mb, meterBeanList);
		
		
		
		/** //实际代码中需要根据meterBookId查询表册及表册下的表计列表.而后以JSON格式返回.
		MeterBookMeterBean meterBookMeter=new MeterBookMeterBean();
		meterBookMeter.setEquipType("JC");
		meterBookMeter.setMeterBookId(1l);
		meterBookMeter.setName("长久花园-10-0");
		
		List<MeterBean> meterList=meterBookMeter.getMeters();
		for(int i=0;i<3;i++) {
			//生成测试的表计数据
			MeterBean tempMeter=new MeterBean();
			tempMeter.setAddr("180303013"+i);   //表计地址
			tempMeter.setMeterId(1L+i);		    //表计ID
			tempMeter.setCustomer("张三"+i);    //客户名称
			tempMeter.setCustomerId(1L+i);      //客户ID
			tempMeter.setLocation("10-"+i);		//地理位置	
			tempMeter.setLastNum("1"+i);		//最后一次读数
			
			//将表计实体加入到表册下的表计列表中
			meterList.add(tempMeter);
		} **/	
		
		Date date1 = new Date();
		Long end = date1.getTime();
		Long d = end-start;
		System.out.println("下载表册用时： "+d+" ms");
		
		return new ResponseEntity<>(meterBookMeter, HttpStatus.OK);
	}
	
	/**
	 * @Title: getMeterBookMeterBean
	 * @Description: 获取表册表计Bean
	 * @param meterBook
	 * @return 
	 */
	private MeterBookMeterBean getMeterBookMeterBean(MeterBook meterBook, List<MeterBean> meterList) {
		
		Date date = new Date();
		Long start = date.getTime();
		if(StringUtils.isNotBlank(meterBook.getFactory()) && meterBook.getFactory().equals(MeterFactoryConstant.METER_FACTORY_EG)) {
			//升序排序 
			this.sortByAddrAsc(meterList);
		}else {
			//升序排序
			this.sortByLocationAsc(meterList);
		}
		Date date2 = new Date();
		Long end = date2.getTime();
		Long d1 = end-start;
		System.out.println("表计集合排序用时："+d1);
		
		MeterBookMeterBean meterBookMeter = new MeterBookMeterBean();
		meterBookMeter.setMeterBookId(meterBook.getId());//表册ID
		meterBookMeter.setName(meterBook.getName());//表册名称 格式：小区名称-楼号-单元
		meterBookMeter.setEquipType(meterBook.getFactory());//表册所对应的设备类型名称 取值包括JC-积成 SC:三川 EG-艺高
		meterBookMeter.setReadType(meterBook.getReadMode());//抄表方式
		meterBookMeter.setMeters(meterList);
		return meterBookMeter;
	}
	
	/**
	 * @Title: sortByAddrAsc
	 * @Description: 根据表地址升序排序
	 * @param meterList 
	 */
	private void sortByAddrAsc(List<MeterBean> meterList) {
		//升序排序
		//System.out.println("排序前：" + meterList);  
        Collections.sort(meterList, new Comparator<MeterBean>() {  
            @Override  
            public int compare(MeterBean o1, MeterBean o2) {  
            	try {
            		String addr1 = "";
            		if(StringUtils.isNotBlank(o1.getCollectorAddr())) {
            			addr1 = addr1+o1.getCollectorAddr();
            		}
            		if(StringUtils.isNotBlank(o1.getAddr())) {
            			addr1 = addr1+o1.getAddr();
            		}
            		
            		String addr2 = "";
            		if(StringUtils.isNotBlank(o1.getCollectorAddr())) {
            			addr2 = addr2+o2.getCollectorAddr();
            		}
            		if(StringUtils.isNotBlank(o1.getAddr())) {
            			addr2 = addr2+o2.getAddr();
            		}
                	//String addr2 = o2.getCollectorAddr()+o2.getAddr();
                	if(StringUtils.isNotBlank(addr1) && StringUtils.isNotBlank(addr2)) {
                		if (Integer.valueOf(addr1)>Integer.valueOf(addr2)) {  
                            return 1;  
                        }  
                        if (Integer.valueOf(addr1)==Integer.valueOf(addr2)) {  
                            return 0;  
                        }  
                	}
                    
                    return -1;  
				} catch (Exception e) {
					e.printStackTrace();
				}
            	return 0;
            }  
        });  
  
        //System.out.println("升序排序后：" + meterList);
	}
	/**
	 * @Title: sortByLocationAsc
	 * @Description: 根据地理位置（房间号）升序排序
	 * @param meterList 
	 */
	private void sortByLocationAsc(List<MeterBean> meterList) {
		//升序排序
		//System.out.println("排序前：" + meterList);  
        Collections.sort(meterList, new Comparator<MeterBean>() {  
            @Override  
            public int compare(MeterBean o1, MeterBean o2) {
            	try {
            		String location1 = o1.getLocation();
                	String location2 = o2.getLocation();
                	if(StringUtils.isNotBlank(location1) && StringUtils.isNotBlank(location2)) {
                		location1 = replaceChinese(location1);//把中文汉字[\u4e00-\u9fa5]替换成空字符串
                		location2 = replaceChinese(location2);//把中文汉字[\u4e00-\u9fa5]替换成空字符串
                		location1 = replaceEnglish(location1);//把英文字母[a-zA-Z]替换成空字符串
                		location2 = replaceEnglish(location2);//把英文字母[a-zA-Z]替换成空字符串
                		location1 = location1.replaceAll("\\-+", "");//把减号（-）替换成空字符串
                		location2 = location2.replaceAll("\\-+", "");//把减号（-）替换成空字符串
                		if (Integer.valueOf(location1)>Integer.valueOf(location2)) {  
                            return 1;  
                        }  
                        if (Integer.valueOf(location1)==Integer.valueOf(location2)) {  
                            return 0;  
                        }  
                	}
                    
                    return -1;  
				} catch (Exception e) {
					e.printStackTrace();
				}
            	return 0;
            }  
        });  
  
        //System.out.println("升序排序后：" + meterList);
	}
	
	/**
	 * @Title: replaceEnglish
	 * @Description: 把中文汉字[\u4e00-\u9fa5]替换成空字符串
	 * @param str
	 * @return 
	 */
	private String replaceChinese(String str) {
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("");
		return repickStr;
	}
	/**
	 * @Title: replaceEnglish
	 * @Description: 把英文字母[a-zA-Z]替换成空字符串
	 * @param str
	 * @return 
	 */
	private String replaceEnglish(String str) {
		String reg = "[a-zA-Z]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("");
		return repickStr;
	}
	
	/**
	 * @Title: getMeterBookMeterList
	 * @Description: 根据表册ID查询表册-表计关系
	 * @param meterBookId
	 * @return 
	 */
	private List<MeterBookMeter> getMeterBookMeterList(Long meterBookId){
		MeterBookMeter mbm = new MeterBookMeter();
		mbm.setMeterBookId(meterBookId);
		return meterBookMeterService.select(mbm);
	}
	
	/**
	 * @Title: getMeterBean
	 * @Description: 获取MeterBean
	 * @param customer
	 * @param meter
	 * @param lastNum
	 * @return 
	 */
	private MeterBean getMeterBean(Customers customer, Meters meter, String lastNum, BigDecimal preAmount) {
		
		String addr = meter.getMeterAddress();//表地址
		String collectorAddr = meter.getCollectorAddr();//采集器地址
		
		MeterBean tempMeter=new MeterBean();
		tempMeter.setAddr(addr);//表计地址
		tempMeter.setCollectorAddr(collectorAddr);//采集器地址
		tempMeter.setMeterId(meter.getId());//表计ID
		tempMeter.setCustomer(customer.getCustomerName());//客户名称
		tempMeter.setCustomerId(customer.getId());//客户ID
		
		//客户类型
		Integer cusotmerType = customer.getCustomerType();
		if(cusotmerType==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {
			tempMeter.setLocation(customer.getRoom());//地理位置	引用客户表中的房间号	
		}else {
			tempMeter.setLocation(meter.getSortValue().toString());//地理位置	引用表计表中的序号	
		}
		
		tempMeter.setLastNum(lastNum);//最后一次读数
		tempMeter.setPreAmount(preAmount);//上期水量
		tempMeter.setMeterNo(meter.getMeterNo());//表计编号
		tempMeter.setDescription(meter.getDescription());//表计描述
		return tempMeter;
	}
	
	/**
	 *
	 * 上传抄表数据
	 * @param meterRead  特定表册的抄表数据
	 * @param request
	 * @param model
	 * @return
	 * 
	 *
	 * 
	 */
	@RequestMapping(value = "/meterReadResult/upload")	
	public ResponseEntity<String> meterReadResultUpload(String meterReadJSON,HttpServletRequest request, Model model) {
		
		try {
			
			MeterReadResultBean meterReadResultBean = FastJsonUtils.getObject(meterReadJSON,MeterReadResultBean.class);
			
			Long userId = meterReadResultBean.getUserId();//抄表员用户ID
			SysUsers user = usersService.selectByPrimaryKey(userId);//抄表员用户
			
			Thread recordUploadTask = new Thread(new Runnable() {
				
				@Override
				public void run() {
					log.debug("----------启动保存APP抄表记录线程");
					saveAppRecord(meterReadResultBean, user);//保存APP抄表记录
				}
			}, "保存 "+user.getRealname()+" 的抄表记录");
	        recordUploadTask.start();
	        
	        return new ResponseEntity<>("ok", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("fail", HttpStatus.OK);
		
	}
	
	/**
	 * @Title: saveAppRecord
	 * @Description: 保存APP抄表记录
	 * @param meterReadResultBean
	 * @param user 
	 */
	private void saveAppRecord(MeterReadResultBean meterReadResultBean, SysUsers user) {
		try {
			
			log.debug("----------开始保存APP抄表记录");
			
			Date sysDate = new Date();//系统日期
			
			//此处并未按RequestPack数据进行打包.
			//测试接收上传表计读数参数
//			MeterReadResultBean meterReadResultBean= FastJsonUtils.getObject(meterReadJSON,MeterReadResultBean.class);
//			
//			Long userId = meterReadResultBean.getUserId();//抄表员用户ID
//			SysUsers user = usersService.selectByPrimaryKey(userId);//抄表员用户
			
			List<MeterRecordTemp> recordTempList = new ArrayList<>();
			
			for(MeterReadBean meterRead : meterReadResultBean.getMeters()){
				Long customerId = meterRead.getCustomerId();//客户ID
				Long meterId = meterRead.getMeterId();//表计ID
				
				MeterRecord lastMeterRecord = meterRecordService.getLastMeterRecord(customerId, null, meterId);//最后一次抄表记录
				if(lastMeterRecord!=null) {
					MeterRecordTemp recordTemp = this.getMeterRecordTempBean(meterRead, lastMeterRecord, user, sysDate);//获取抄表记录临时表Bean
					recordTempList.add(recordTemp);
				}
				
			}

			if(recordTempList!=null && recordTempList.size()>0) {
				int rows = meterRecordTempService.insertBatch(recordTempList);//批量增加
				if(rows<=0) {
					log.debug("批量保存APP抄表记录出错");
				}
			}
			log.debug("----------保存APP抄表记录完成");
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("----------保存APP抄表记录异常");
		}
		
	}
	
	/**
	 * @Title: fileUpload
	 * @Description: 手机APP接口上传图片文件
	 * @param request
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/file/upload")	
	public ResponseEntity<String> fileUpload(HttpServletRequest request, Model model) {
		
		//Date sysDate = new Date();//系统日期
		String fileDir = "app img";//上传图片类型，系统会根据此类型增加对应目录
		
		try {
			String userId = request.getParameter("userId");
			String customerId = request.getParameter("customerId");
			String meterId = request.getParameter("meterId");
			log.info("userId:"+userId+", customerId:"+customerId+", meterId:"+meterId);

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> fileList = multipartRequest.getFiles("imageFile");
			if(fileList!=null && fileList.size()>0) {
				MultipartFile file = fileList.get(0);
				if (file!=null) {
					String uploadPath = null;
					try {
						uploadPath = FileUploadUtil.uploadFile(multipartRequest, file, uploadFileConfig.getUploadFolder(), fileDir);
					} catch (IOException e) {
						e.printStackTrace();
						log.info("上传文件异常，"+e.getMessage(), e);
					}
					if(StringUtils.isNotBlank(uploadPath)){
						log.info("上传文件成功   文件保存路径:"+uploadPath);
						
						///home/upload/app img/20190724/2019072419474649719058.jpg
						//upload/app img/20190724/2019072419474649719058.jpg
						uploadPath = "/"+uploadPath.substring(uploadPath.indexOf("upload"));
						
						String imageFormat = uploadPath.substring(uploadPath.lastIndexOf("."));//图片格式
						
						MeterRecordTempPhoto tempPhoto = new MeterRecordTempPhoto();
						tempPhoto.setCustomerId(Long.valueOf(customerId));
						tempPhoto.setMeterId(Long.valueOf(meterId));
						tempPhoto.setOperatorId(Long.valueOf(userId));
						tempPhoto.setOperationTime(new Date());
						tempPhoto.setImagePath(uploadPath);
						tempPhoto.setImageFormat(imageFormat);
						
						int rows = meterRecordTempPhotoService.insertSelective(tempPhoto);
						if(rows>0) {
							return new ResponseEntity<>("ok", HttpStatus.OK);
						}
					}
	    		}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("fail", HttpStatus.OK);
		
	}
	
	/**
	 * @Title: getMeterRecordTempBean
	 * @Description: 获取抄表记录临时表Bean
	 * @param meterRead
	 * @param lastMeterRecord
	 * @param user
	 * @param sysDate
	 * @return 
	 */
	private MeterRecordTemp getMeterRecordTempBean(MeterReadBean meterRead, MeterRecord lastMeterRecord, SysUsers user, Date sysDate) {
		
		String preRead = "0";//上期表读数
		Date preDate = DateUtils.addMonths(sysDate, -1);//上期抄表日期
		if(lastMeterRecord==null) {
			Long meterId = meterRead.getMeterId();//表计ID
			Meters meter = metersService.selectByPrimaryKey(meterId);
			if(meter!=null && StringUtils.isNotBlank(meter.getNewMeterBottom())) {
				preRead = meter.getNewMeterBottom();//上期表读数
			}
		}else {
			if(StringUtils.isNotBlank(preRead)) {
				preRead = lastMeterRecord.getCurrRead();
			}
			preDate = lastMeterRecord.getCurrDate();
		}
		
		MeterRecordTemp temp = new MeterRecordTemp();
		temp.setCustomerId(meterRead.getCustomerId());
		temp.setMeterId(meterRead.getMeterId());
		
		temp.setPreRead(preRead);
		temp.setPreDate(preDate);
		
		String meterNum = meterRead.getMeterNum();//本期抄表读数
		
		if(StringUtils.isNotBlank(meterNum)) {
			
			
			//获取本期水量
			BigDecimal currWaterAmount = new BigDecimal(0);
			try {
				currWaterAmount = this.getCurrWaterAmount(meterRead.getMeterNum(), preRead);
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("获取当前水量异常");
			}
			
			temp.setCurrRead(meterRead.getMeterNum());
			temp.setCurrDate(DateUtils.parseDate(meterRead.getReadDate()));
			temp.setCurrAmount(currWaterAmount);
		}
		
		temp.setOperatorId(user.getId());
		temp.setOperatorName(user.getRealname());
		temp.setOperationTime(sysDate);
		
		//设置抄表方式和抄表结果状态
		this.setReadResultStatus(temp, meterRead.getReadState());
		
		temp.setRemark("手机APP抄表上传数据");
		
		return temp;
	}
	
	/**
	 * @Title: setReadResultStatus
	 * @Description: 设置抄表方式和抄表结果状态
	 * @param recordTemp
	 * @param status 
	 */
	private void setReadResultStatus(MeterRecordTemp recordTemp, Integer status) {
		//0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表
//		Integer status = meterRead.getReadState();
		if(status==EnumAppReadResult.RESULT_NO.getValue()) {//0:尚未抄表
			recordTemp.setReadMode(EnumReadMode.READ_BLUETOOTH.getCode());
			recordTemp.setReadResult(EnumAppReadResult.RESULT_NO.getValue());
		}else if(status==EnumAppReadResult.RESULT_NORMAL.getValue()) {//1:自动:正确读表
			recordTemp.setReadMode(EnumReadMode.READ_BLUETOOTH.getCode());
			recordTemp.setReadResult(EnumAppReadResult.RESULT_NORMAL.getValue());
		}else if(status==EnumAppReadResult.RESULT_READ_ERROR.getValue()) {//2:自动:表计读数错误
			recordTemp.setReadMode(EnumReadMode.READ_BLUETOOTH.getCode());
			recordTemp.setReadResult(EnumAppReadResult.RESULT_READ_ERROR.getValue());
		}else if(status==EnumAppReadResult.RESULT_NO_TIMEOUT.getValue()) {//3:自动:未抄到(超时引起)
			recordTemp.setReadMode(EnumReadMode.READ_BLUETOOTH.getCode());
			recordTemp.setReadResult(EnumAppReadResult.RESULT_NO_TIMEOUT.getValue());
		}else if(status==EnumAppReadResult.RESULT_MANUAL.getValue()) {//4:手工抄表
			recordTemp.setReadMode(EnumReadMode.READ_MANUAL.getCode());
			recordTemp.setReadResult(EnumAppReadResult.RESULT_MANUAL.getValue());
		}
	}
	
	/**
	 * @Title: getCurrWaterAmount
	 * @Description: 获取当前水量
	 * @param currRead
	 * @param preRead
	 * @return 
	 */
	private BigDecimal getCurrWaterAmount(String currRead, String preRead) throws Exception {
		
		log.debug("本期表底："+currRead);
		log.debug("上期表底："+preRead);
		
		BigDecimal b1 = new BigDecimal(currRead);
		BigDecimal b2 = new BigDecimal(preRead);
		
		BigDecimal b3 = BigDecimalUtils.subtract(b1, b2);
		return b3;
	}
	
	public static void main(String[] args) {
		List<MeterBean> list = new ArrayList<MeterBean>();
		
		for(int i=10; i<20; i++) {
			MeterBean meter1 = new MeterBean();
			meter1.setAddr("0"+i);
			list.add(meter1);
			for(int j=30; j<40; j++) {
				MeterBean meter = new MeterBean();
				meter.setAddr("0"+j);
				list.add(meter);
			}
		}
		
		
		for(MeterBean bean : list) {
			System.out.print(bean.getAddr()+" ");
		}
		
		//sort(list);
		
		System.out.println(" ");
		for(MeterBean bean : list) {
			System.out.print(bean.getAddr()+" ");
		}
		
		
		String str = "/home/upload/app img/20190724/2019072419474649719058.jpg";
		System.out.println(str.substring(str.indexOf("upload")));
		
	}
	
}