/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;
        _colNameIndex = input.colNameToIndex(_colName);
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colNameIndex).equals(_match)) {
            return true;
        }
        return false;
    }

    private int _colNameIndex;

    private String _colName, _match;
}
