package com.ryx.swiper.devs.newland.common;



public  class CardData{
	private static CardData mCardData=null;
	private String cardNo;//卡号
	private String cardSequenceNumber;  //卡序列号
	private byte[] tag_55;
	
	private byte[] secondTrack;   //二磁道密文
	private byte[] thirdTrack;      //三磁道密文
	
	private  CardData() {			
	}
	
	public static CardData getInstance() {
//		synchronized (MainActivity.this) {
			if (mCardData==null) {
				mCardData=new CardData();
			}
//		}
		return mCardData;
	}
	
	public String getCardNo() {
		return mCardData.cardNo;
	}
	public void setCardNo(String cardNo) {
		mCardData.cardNo = cardNo;
	}
	/**
	 * @return the cardSequenceNumber
	 */
	public String getCardSequenceNumber() {
		return mCardData.cardSequenceNumber;
	}
	/**
	 * @param cardSequenceNumber the cardSequenceNumber to set
	 */
	public void setCardSequenceNumber(String cardSequenceNumber) {
		mCardData.cardSequenceNumber = cardSequenceNumber;
	}
	
	public byte[] getTag_55() {
		return mCardData.tag_55;
	}
	public void setTag_55(byte[] tag_55) {
		mCardData.tag_55 = tag_55;
	}
	public byte[] getSecondTrack() {
		return mCardData.secondTrack;
	}
	public void setSecondTrack(byte[] secondTrack) {
		mCardData.secondTrack = secondTrack;
	}
	public byte[] getThirdTrack() {
		return mCardData.thirdTrack;
	}
	public void setThirdTrack(byte[] thirdTrack) {
		mCardData.thirdTrack = thirdTrack;
	}
	
}