/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colName = colName;
        _ref = ref;
        _colNameIndex = input.colNameToIndex(_colName);
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colNameIndex).compareTo(_ref) >= 0) {
            return true;
        }
        return false;
    }

    String _colName, _ref;

    int _colNameIndex;
}
