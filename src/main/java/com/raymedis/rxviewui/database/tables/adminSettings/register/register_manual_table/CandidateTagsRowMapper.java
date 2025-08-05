package com.raymedis.rxviewui.database.tables.adminSettings.register.register_manual_table;


import com.raymedis.rxviewui.database.GenericRepository;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateTagsRowMapper implements GenericRepository.RowMapper<CandidateTagsEntity, String>{
    @Override
    public CandidateTagsEntity mapRow(ResultSet rs) throws SQLException {
        CandidateTagsEntity candidateTags = new CandidateTagsEntity();
        candidateTags.setTagName(rs.getString("tagName"));
        candidateTags.setSelected(rs.getBoolean("isSelected"));
        return candidateTags;
    }

    @Override
    public Object getValue(CandidateTagsEntity entity, String columnName) {
        if (columnName.equals("isSelected")) {
            return entity.getSelected();
        }
        return null;
    }

    @Override
    public Object getPrimaryKeyValue(CandidateTagsEntity entity, String primaryKeyName) {
        return entity.getTagName() == null ? "" : entity.getTagName();
    }

}
