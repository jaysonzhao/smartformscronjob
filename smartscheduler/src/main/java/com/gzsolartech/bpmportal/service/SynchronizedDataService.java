package com.gzsolartech.bpmportal.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.entity.FMoney;
import com.gzsolartech.smartforms.exceptions.SmartformsException;
import com.gzsolartech.smartforms.service.BaseDataService;

/**
 * @description sql server to oracle
 * @author hhf
 * @date 2017年5月24日 下午6:47:55
 */
@Service
public class SynchronizedDataService extends BaseDataService{

	private static final long serialVersionUID = 5111237443230279743L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SynchronizedDataService.class);
	
	public void execute() throws SmartformsException{
		try {
			String url = "jdbc:sqlserver://10.128.113.141:1433;databaseName=FinanceSetting;IntegratedSecurity=False";
			Connection con = DriverManager.getConnection(url,"eipReader","eipReader");
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from f_Money");
			ArrayList<FMoney> data = new ArrayList<FMoney>();
			while (resultSet.next()) {
				FMoney fMoney = new FMoney();
				fMoney.setId(resultSet.getInt("Id"));
				fMoney.setMoneyType(resultSet.getString("MoneyType"));
				fMoney.setMoneyCode(resultSet.getString("MoneyCode"));
				fMoney.setMoneyRate(resultSet.getString("MoneyRate"));
				fMoney.setMoneySign(resultSet.getString("MoneySign"));
				fMoney.setSort(resultSet.getInt("Sort"));
				fMoney.setCreator(resultSet.getString("Creator"));
				fMoney.setCreationDate(resultSet.getTimestamp("CreationDate"));
				data.add(fMoney);
			}
			gdao.deleteByHql("delete from FMoney");
			gdao.save(data);
		} catch (Exception e) {
			LOGGER.error("同步数据时出现错误：",e);
			throw new SmartformsException(e);
		}
	}
}
