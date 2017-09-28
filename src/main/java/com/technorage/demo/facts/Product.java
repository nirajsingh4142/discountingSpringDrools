/**
 * 
 */
package com.technorage.demo.facts;
/**
 * @author raghav.rampal
 *
 */
public class Product {
	
	private String massProductGroupCode;
	
	private String productGroupCode;
	
	private String familyCode;
	
	private Integer isbn;
	
	private Integer offerCode;
	
	public String getMassProductGroupCode() {
		return massProductGroupCode;
	}
	
	public void setMassProductGroupCode(String massProductGroupCode) {
		this.massProductGroupCode = massProductGroupCode;
	}
	
	public String getProductGroupCode() {
		return productGroupCode;
	}
	
	public void setProductGroupCode(String productGroupCode) {
		this.productGroupCode = productGroupCode;
	}
	
	public String getFamilyCode() {
		return familyCode;
	}
	
	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	public Integer getIsbn() {
		return isbn;
	}

	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}

	public Integer getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(Integer offerCode) {
		this.offerCode = offerCode;
	}

}
