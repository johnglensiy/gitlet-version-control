import java.util.List;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _colName1 = colName1;
        _colName2 = colName2;
        _colName1Index = input.colNameToIndex(colName1);
        _colName2Index = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        //Table.TableRow candidateNext = candidateNext();

        if (candidateNext().getValue(_colName1Index).equals
                (candidateNext().getValue(_colName2Index))) {
            return true;
        }
        return false;
    }

    private int _colName1Index, _colName2Index;

    private String _colName1, _colName2;
}
