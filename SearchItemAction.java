package com.internousdev.spring.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.spring.dao.McategoryDAO;
import com.internousdev.spring.dao.ProductInfoDAO;
import com.internousdev.spring.dto.McategoryDTO;
import com.internousdev.spring.dto.ProductInfoDTO;
import com.internousdev.spring.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class SearchItemAction extends ActionSupport implements SessionAware {
	// カテゴリーの選択肢が存在しないとき（全てのカテゴリー）に
	// 全件検索をしたい為、nullを入れられる文字型でcategoryIdを定義
	private String categoryId;

	private String keywords;// 検索ワード
	private List<String> errorMessageList;// 商品情報に入ってない文字列を入力してない時にエラーメッセージ専用
	// の商品一覧画面に遷移させる為に定義

	private List<ProductInfoDTO> productInfoDTOList;
	private Map<String, Object> session;

	public String execute() throws SQLException {

		if (StringUtils.isBlank(keywords)) {
			// 検索キーワードが nullか空か空白であれば空文字を設定
			keywords = "";
		} else {
			// 検索ワードがあれば以下の処理
			InputChecker inputChecker = new InputChecker();
			// 入力チェック（半角英字、漢字、ひらがな、半角数字、カタカナ、スペースのみの入力を受け付ける）
			errorMessageList = inputChecker.doCheck("検索ワード", keywords, 0, 50, true, true, true, true, true, true);

			// エラーメッセージのみの商品一覧画面に遷移
			if (errorMessageList.size() > 0) {
				return SUCCESS;
			}

			// キーワードの" "を" "に変換して" "2個以上を" "に変換し、前後のスペースを削除
			keywords = keywords.replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		}

		// カテゴリーの選択肢が存在しない場合は、すべてのカテゴリー（１）を設定する
		if (categoryId == null) {
			categoryId = "1";
		}

		ProductInfoDAO productInfoDAO = new ProductInfoDAO();

		switch (categoryId) {
		// 全てのカテゴリーの場合、全件検索
		case "1":
			productInfoDTOList = productInfoDAO.getProductInfobyKeywords(keywords.split(" "));
			break;

		// それ以外（カテゴリー情報テーブル準拠（2～5））の場合は条件検索
		default:
			productInfoDTOList = productInfoDAO.getProductInfobyCategoryIdandKeywords(Integer.parseInt(categoryId),
					keywords.split(" "));
			break;
		}

		// カテゴリーリストがなければカテゴリー情報を入れる（保持させる）
		if (!session.containsKey("mCategoryDTOList")) {
			List<McategoryDTO> mCategoryDTOList = new ArrayList<McategoryDTO>();
			McategoryDAO mCategoryDAO = new McategoryDAO();
			mCategoryDTOList = mCategoryDAO.getMcategoryInfo();

			session.put("mCategoryDTOList", mCategoryDTOList);
		}

		return SUCCESS;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<String> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(List<String> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}

	public List<ProductInfoDTO> getProductInfoDTOList() {
		return productInfoDTOList;
	}

	public void setProductInfoDTOList(List<ProductInfoDTO> productInfoDTOList) {
		this.productInfoDTOList = productInfoDTOList;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}