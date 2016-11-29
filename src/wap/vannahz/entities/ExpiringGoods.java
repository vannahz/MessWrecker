package wap.vannahz.entities;

import java.util.Date;

public class ExpiringGoods {
	
	
	int prodect_id;
	String product_type;
	String product_name;
	Date production_date;
	Date expiration_date;
	int total_amount;
	int left_days;
	Date record_time;
	
	public Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public ExpiringGoods(){
	}

	public int getProdect_id() {
		return prodect_id;
	}

	public void setProdect_id(int prodect_id) {
		this.prodect_id = prodect_id;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Date getProduction_date() {
		return production_date;
	}

	public void setProduction_date(Date production_date) {
		this.production_date = production_date;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}

	public int getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}

	public int getLeft_days() {
		return left_days;
	}

	public void setLeft_days(int left_days) {
		this.left_days = left_days;
	}

}
