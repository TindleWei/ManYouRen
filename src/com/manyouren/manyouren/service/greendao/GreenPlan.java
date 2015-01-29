/**
* @Package com.manyouren.android.service.greendao    
* @Title: GreenPlan.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-7 上午2:53:05 
* @version V1.0   
*/
package com.manyouren.manyouren.service.greendao;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-7 上午2:53:05 
 *  
 */
@Deprecated
public class GreenPlan {

//	private static GreenPlan instance;
//	
//	private PlanDao planDao;
//	
//	private GreenPlan(){
//		
//	}
//	
//	public static GreenPlan getInstance(Context context){
//		if(instance == null){
//			instance = new GreenPlan();
//			DaoSession daoSession = RootApplication.getDaoSession(context);
//			instance.planDao = daoSession.getPlanDao();
//		}
//		return instance;
//	}
//	
//	public boolean isSaved(long Id) {
//		QueryBuilder<Plan> qb = planDao.queryBuilder();
//		qb.where(PlanDao.Properties.Id.eq(Id));
//		qb.buildCount().count();
//		return qb.buildCount().count() > 0 ? true : false;// 查找收藏表
//	}
//	
//	public void insertPlan(Plan entity){
//		planDao.insert(entity);
//	}
//	
//	/** 
//	* insert or update
//	*
//	* @param entity
//	* @return long
//	*/
//	public long savePlan(Plan entity){
//		return planDao.insertOrReplace(entity);
//	}
//	
//	public List<Plan> getAllPlan(){
//		return planDao.loadAll();
//	}
//	
//	public int getPlanCount(){
//		QueryBuilder<Plan> qb = planDao.queryBuilder();
//		
//		return (int)qb.count();
//	}
//	
//	public List<PlanEntity> getRecentPlan(){
//		QueryBuilder<Plan> qb = planDao.queryBuilder();
//		//qb.orderDesc(Properties.Id);
//
//		int num = (int)qb.count();
//		
//		if(num>20){
//			deleteList(num, qb);
//			
//			return changeGreenToPlan(qb.limit(20).list());
//		}else{
//
//			return changeGreenToPlan(qb.limit(num).list());
//		}
//	}
//	
//	
//	public void deleteList(int num, QueryBuilder<Plan> qb){
//		List<Plan> list = qb.list().subList(0, num-20);
//		Iterator<Plan> it = list.iterator();
//		while(it.hasNext()){
//			planDao.delete(it.next());
//		}
//	}
//	
//	private static List<PlanEntity> changeGreenToPlan(List<Plan> list) {
//
//		List<PlanEntity> planList = new ArrayList<PlanEntity>();
//
//		Iterator<Plan> it = list.iterator();
//		while(it.hasNext()){
//			Plan plan = (Plan)it.next();
//			PlanEntity entity = new PlanEntity();
//			entity.setUserId(plan.getUserId());
//			entity.setCreateTime(plan.getCreateTime());
//			entity.setDestination(plan.getDestination());
//			entity.setCity(plan.getCity());
//			entity.setStartCity(plan.getStartCity());
//			entity.setStartDate(plan.getStartDate());
//			entity.setEndDate(plan.getEndDate());
//			entity.setVehicle(plan.getVehicle());
//			entity.setTogether(plan.getTogether());
//			entity.setPurpose(plan.getPurpose());
//			entity.setType(plan.getType());
//			entity.setImages(plan.getImages());
//			entity.setFlightNumber(plan.getFlightNumber());
//			entity.setPostscript(plan.getPostscript());
//			entity.setValidate(plan.getValidate());
//			entity.setLikeThis(plan.getLikeThis());
//			entity.setLatitude(plan.getLatitude());
//			entity.setLongitude(plan.getLongitude());
//			entity.setUserEntity(UserController.getUserById(RootApplication.getInstance(), plan.getUserId()));
//			
//			planList.add(entity);
//		}
//		return planList;
//	}
//	
//	public void deleteById(long Id)
//    {
//        QueryBuilder<Plan> qb = planDao.queryBuilder();
//        DeleteQuery<Plan> bd = qb.where(PlanDao.Properties.Id.eq(Id)).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//	
//	public void deleteAll(){
//		planDao.deleteAll();
//	}


}
