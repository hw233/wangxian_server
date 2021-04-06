package com.xuanzhi.tools.objectdb;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.xuanzhi.tools.objectdb.entity.Person;
import com.xuanzhi.tools.text.DateUtil;

import java.io.*;
import java.util.*;

public class ObjectDBTest {

	public static void main(String args[]) throws Exception{
		
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("./data/test_objectdb.odb");
		
		int n = 1000000;
		
		int count = 0;
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		long now = System.currentTimeMillis();
		for(int i = 0 ; i < n ; i++){
			String s = "" + i;
			while(s.length() < 8){
				s = "0"+s;
			}
			s = "139"+s;
			
			Person p = new Person("name-"+i,i+1,i%2,"打孔类；大厦开发发生的房间里看撒娇发拉克沙发"+i+"dsafsa",s);
			em.persist(p);
			count ++;
			if(count == 10000){
				em.getTransaction().commit();
				em.close();
				
				String minfo = (Runtime.getRuntime().maxMemory()/1024/1024)+"m/" + (Runtime.getRuntime().totalMemory()/1024/1024)+"m/"+(Runtime.getRuntime().freeMemory()/1024/1024)+"m";
				
				System.out.println("store "+count+" Person to objectdb and cost "+(System.currentTimeMillis() - now)+" ms. Memory: " + minfo);
				
				em = emf.createEntityManager();

				if(i < n){
					now = System.currentTimeMillis();
					count = 0;
					em.getTransaction().begin();
				}
			}
		}
		
		
		
		if(count > 0){
			em.getTransaction().commit();
			System.out.println("store "+count+" Person to objectdb and cost "+(System.currentTimeMillis() - now)+" ms");
		}
		
		now = System.currentTimeMillis();
		Query q1 = em.createQuery("select count(p) from Person p");
		System.out.println("query Total Person Num = "+q1.getSingleResult()+" from objectdb and cost "+(System.currentTimeMillis() - now)+" ms");
		
		//now = System.currentTimeMillis();
		//Query q1 = em.createQuery("select p from Person p where p.name= ");
		//System.out.println("query Total Person Num = "+q1.getSingleResult()+" from objectdb and cost "+(System.currentTimeMillis() - now)+" ms");
		
		
		
	}
}
