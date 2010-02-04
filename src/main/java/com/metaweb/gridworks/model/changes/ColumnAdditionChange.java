package com.metaweb.gridworks.model.changes;

import java.util.List;

import com.metaweb.gridworks.model.Column;
import com.metaweb.gridworks.model.Project;

public class ColumnAdditionChange extends ColumnChange {
    private static final long serialVersionUID = -3938837464064526052L;
    
    final protected String          _headerLabel;
    final protected int             _columnIndex;
    final protected CellAtRow[]     _newCells;
    protected int                   _newCellIndex;
    
    public ColumnAdditionChange(String headerLabel, int columnIndex, List<CellAtRow> newCells) {
        _headerLabel = headerLabel;
        _columnIndex = columnIndex;
        _newCells = new CellAtRow[newCells.size()];
        newCells.toArray(_newCells);
    }

    @Override
    public void apply(Project project) {
        synchronized (project) {
            _newCellIndex = project.columnModel.allocateNewCellIndex();
            
            Column column = new Column(_newCellIndex, _headerLabel);
            
            project.columnModel.columns.add(_columnIndex, column);
            
            for (CellAtRow cell : _newCells) {
                project.rows.get(cell.row).cells.set(_newCellIndex, cell.cell);
            }
        }
    }

    @Override
    public void revert(Project project) {
        synchronized (project) {
            for (CellAtRow cell : _newCells) {
                project.rows.get(cell.row).cells.remove(_newCellIndex);
            }
            
            project.columnModel.columns.remove(_columnIndex);
        }
    }

}