/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colName = colName;
        _subStr = subStr;
        _colNameIndex = input.colNameToIndex(_colName);

    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(_colNameIndex).contains(_subStr)) {
            return true;
        }
        return false;
    }

    private String _colName, _subStr;

    private int _colNameIndex;
}
