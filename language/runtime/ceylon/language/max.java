package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class max {
    
    private max() {
    }
    
    @TypeParameters(@TypeParameter(value="Value", satisfies="ceylon.language.Comparable<Value>"))
    @TypeInfo("Value")
    public static <Value extends Comparable<? super Value>>Value max(@Name("values")
    @TypeInfo("ceylon.language.Sequence<Value>")
    final Sequence<? extends Value> values) {
        Value max = values.getFirst();
        for (Iterator<? extends Value> $val$iter$0 = values.getRest().getIterator(); $val$iter$0 != null; $val$iter$0 = $val$iter$0.getTail()) {
            final Value val = $val$iter$0.getHead();
            if (val.compare(max).largerThan()) {
                max = val;
            }
        }
        return max;
    }
}
