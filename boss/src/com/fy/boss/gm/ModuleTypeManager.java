package com.fy.boss.gm;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.gm.ModuleType;
import com.fy.boss.gm.ModuleTypeManager;
import com.fy.boss.gm.ModuleTypeUtil;
import com.fy.boss.gm.XmlModule;
public class ModuleTypeManager {
      protected static Logger logger = Logger.getLogger(ModuleTypeManager.class.getName());
      //保存的路径
	  protected String typeConfFile;
	  //保存所有的mokuai
	  private List<ModuleType> types=new ArrayList<ModuleType>();
	  //保存所有的页面模块
	  private List<XmlModule> modules = new ArrayList<XmlModule>();
	  //保存所有的模块id
	  private List<String> moduleids = new ArrayList<String>();
 	  private static ModuleTypeManager self;
	  public static ModuleTypeManager getInstance(){
		  return self;
	  }
	  public void initialize()  {
			//......
			long now = System.currentTimeMillis();
			try {
				types = ModuleTypeUtil.loadPage(typeConfFile);
				for(ModuleType t :types){
					modules.addAll(t.getModules());
				}
				for(XmlModule m :modules){
					moduleids.add(m.getId());
				}
				self = this;
				System.out.println(this.getClass().getName() + " initialize successfully ["+typeConfFile+"] [types:"+types.size()+"] ["+(System.currentTimeMillis()-now)+"]");
				logger.info(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"][saveFile:"+typeConfFile+"]");
			} catch (Exception e) {
				logger.info(this.getClass().getName() + " initialize fail [saveFile:"+typeConfFile+"]");
			}
		}
	  public XmlModule getModule(String id){
		  //通过id获取xmlmodule
		  XmlModule module = null;
		  for(XmlModule mo:modules){
			  if(id.equals(mo.getId())){
				  module = mo;
				  break;  
			  }
		  }
		  return module;
	  }
	  public ModuleType getType(int id){
		  //通过id获取moduletype
		  ModuleType type = null;
		  for(ModuleType t:types){
			  if(id==t.getId()){
				  type = t;
				  break;  
			  }
		  }
		  return type;
	  }
	  
	  public void save(){
		  //保存
		  try {
			ModuleTypeUtil.savePage(types, typeConfFile);
			logger.info(this.getClass().getName() + " save success [saveFile:"+typeConfFile+"]");
		} catch (Exception e) {
			logger.info(this.getClass().getName() + " save fail [saveFile:"+typeConfFile+"]");
		}
	  }
	  public void insertType(ModuleType type,int typeid){
      //根据typeid和新的type对象新增type
		  int id = typeIdGenerator();
		   type.setId(id);
		  try {
			  if(typeid==-1)
				  types.add(type);
			  else
			    types.add(getIndex(typeid,types), type);
			  save();
			logger.info("module: ["+type.getName()+"] insert success at"+new java.util.Date()); 
		} catch (Exception e) {
			logger.info("module: ["+type.getName()+"] insert fail at"+new java.util.Date()); 
		}
	  }
	  private int getIndex(int id,List<ModuleType> ts){
		  //根据id寻找type的位置
		  int index = ts.size();
		  for(int i =0;i<ts.size();i++){
			 ModuleType t = ts.get(i);
			  if(id == t.getId()){
				  index = i;
				  break;  
			  }
		  }
		  return index;
	  }
	  private int getModuleIndex(ModuleType t,int mid){
		  //根据id寻找type的位置
		  int index = t.getModules().size();
		  for(int i =0;i<t.getModules().size();i++){
			  XmlModule m = t.getModules().get(i);
			  if((mid+"").equals( m.getId())){
				  index = i;
				  break;  
			  }
		  }
		  return index;
	  }
	  public void insertModule(XmlModule module,int moduleid,int typeid){
		  
		  ModuleType type= getType(typeid);
		  try {//测试是否是值改变了
			module.setId(idGenerator());
			if(moduleid==-1)
			type.getModules().add( module);
			else
			type.getModules().add(getModuleIndex(type, moduleid), module);	
			modules.add(module );
			moduleids.add(module.getId());
			save();
			logger.info("module: ["+module.getId()+"to "+type.getName()+"] insert success at"+new java.util.Date()); 
		} catch (Exception e) {
			logger.info("module: ["+module.getId()+"to "+type.getName()+"] insert fail at"+new java.util.Date()); 
		}
	  }
	  public void updateModule(XmlModule module,int typeid){
		  //修改模块
		    ModuleType mt = getType(typeid);
		    int index = getModuleIndex(mt, Integer.parseInt(module.getId()));
		    mt.getModules().set(index,module);
		    for(int i =0;i<modules.size();i++){
		    	 if(modules.get(i).getId().equals(module.getId())){
			          modules.set(i,module );
			     }
			}
		   save();
		   logger.info("module :["+module.getId()+"] at "+index+" update success at "+new java.util.Date() );
	  }
	  public void updateType(ModuleType type){
		  //修改type名
		   types.set(getIndex(type.getId(), types), type);
		   save();
		   logger.info("type :["+type.getName()+"] update success  " );
	  }
	  
	  public void  deleteModule(String moduleid,int typeid){
		  //删除模块
		  try {
			   ModuleType mt = getType(typeid);       
			   mt.getModules().remove(getModule(moduleid));
					   save();
				
			   for(XmlModule xm:modules){
				   if(moduleid.equals(xm.getId())){
					   modules.remove(xm);	   
					   moduleids.remove(xm.getId());   
					   break;
				   }
			   }
			logger.info("module :["+moduleid+"] delete success at "+new java.util.Date());
		} catch (Exception e) {
			logger.info("");
		}
	  }
	  public void  deleteType(int typeid){
		  //根究typeid删除一个模块
		  try {
			ModuleType t = getType(typeid);
			types.remove(t);
			modules.removeAll(t.getModules());
			for(XmlModule m:t.getModules()){
				moduleids.remove(m.getId());
			}
			save();
			logger.info("type :["+t.getName()+"] delete success at "+new java.util.Date());
		} catch (Exception e) {
			logger.info("");
		}
	  }
	  public String idGenerator(){
		  long id=1000;
		  for(XmlModule mm:modules){
			  long m = Long.parseLong(mm.getId());
			  if(m>=id)
				  id=m+1;
		  }
		  return id+"";
	  }
	  public int typeIdGenerator(){
		  int id = 1000;
		  for(ModuleType t : types){
			  int id1 = t.getId();
			  if(id1>=id)
				  id = id1+1;
		  }
		  return id ;
	  }
		public String getTypeConfFile() {
			return typeConfFile;
		}
		public void setTypeConfFile(String typeConfFile) {
			this.typeConfFile = typeConfFile;
		}
		public List<ModuleType> getTypes() {
			return types;
		}
		public void setTypes(List<ModuleType> types) {
			this.types = types;
		}
		public List<XmlModule> getModules() {
			return modules;
		}
		public void setModules(List<XmlModule> modules) {
			this.modules = modules;
		}
		public List<String> getModuleids() {
			return moduleids;
		}
		public void setModuleids(List<String> moduleids) {
			this.moduleids = moduleids;
		}
	    
	}
