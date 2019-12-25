package com.internousdev.spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.spring.dto.McategoryDTO;
import com.internousdev.spring.util.DBConnector;

public class McategoryDAO {
	/**
	 * McategoryDTOからカテゴリーマスターテーブルの情報を持ってくる。
	 *
	 * @return mcategoryDTOList
	 * @throws SQLException 
	 */

	public List<McategoryDTO> getMcategoryInfo() throws SQLException {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<McategoryDTO> mcategoryDTOList = new ArrayList<McategoryDTO>();

		String sql = "SELECT * from m_category";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				McategoryDTO mcategoryDTO = new McategoryDTO();
				mcategoryDTO.setId(rs.getInt("id"));
				mcategoryDTO.setCategoryId(rs.getInt("category_id"));
				mcategoryDTO.setCategoryName(rs.getString("category_name"));
				mcategoryDTO.setCategoryDescription(rs.getString("category_description"));
				mcategoryDTOList.add(mcategoryDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				con.close();
	
		}
		return mcategoryDTOList;

	}
}
