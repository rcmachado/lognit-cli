package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.AggregatedItemHelper;
import net.intelie.lognit.cli.UserConsole;
import net.intelie.lognit.cli.model.Aggregated;
import net.intelie.lognit.cli.model.AggregatedItem;
import net.intelie.lognit.cli.model.Message;
import net.intelie.lognit.cli.model.SearchStats;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.*;

import static org.mockito.Mockito.*;

public class ColoredFormatterTest {

    private ColoredFormatter printer;
    private UserConsole console;
    private BarsFormatter bars;

    private static final String YELLOW = "\033[33m";
    private static final String GREEN = "\033[32m";
    private static final String CYAN = "\033[36m";
    private static final String NONE = "\033[0m";

    @Before
    public void setUp() throws Exception {
        console = mock(UserConsole.class);
        bars = mock(BarsFormatter.class);
        printer = new ColoredFormatter(console, bars);
    }

    @Test
    public void hasConsoleOnlyConstructor() throws Exception {
        new ColoredFormatter(mock(UserConsole.class));
    }

    @Test
    public void testPrintMessage() throws Exception {
        when(console.isTTY()).thenReturn(true);

        Map metadata = new HashMap();
        metadata.put("abc", Arrays.asList("111", "222"));
        metadata.put("qwe", Arrays.asList("333"));

        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", metadata);
        printer.print(message);
        verify(console).printOut(colored("$cA$n $g1111-11-11 11:11:11$n D E $yF$n abc $cabc:$n111$c,$n222 $cqwe:$n333"));
    }

    private String colored(String s) {
        return s.replace("$c", CYAN).replace("$g", GREEN).replace("$y", YELLOW).replace("$n", NONE);
    }

    private String nonColored(String s) {
        return s.replace("$c", "").replace("$g", "").replace("$y", "").replace("$n", "");
    }

    @Test
    public void testAggregated() throws Exception {
        when(console.isTTY()).thenReturn(true);
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123, "abd", 42);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124, "abd", "qwe");
        Aggregated aggr = new Aggregated(item1, item2);

        printer.print(aggr);
        verify(console).printOut(colored("abc:$g123$n abd:$g42$n"));
        verify(console).printOut(colored("abc:$g124$n abd:$gqwe$n"));
    }

    @Test
    public void testPrintMessageNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);

        Map metadata = new HashMap();
        metadata.put("abc", Arrays.asList("111", "222"));
        metadata.put("qwe", Arrays.asList("333"));

        Message message = new Message("123", "A", "11111111", "111111", "D", "E", "F", "abc", metadata);
        printer.print(message);
        verify(console).printOut(nonColored("$cA$n $g1111-11-11 11:11:11$n D E $yF$n abc $cabc:$n111$c,$n222 $cqwe:$n333"));
    }

    @Test
    public void testAggregatedNoTty() throws Exception {
        when(console.isTTY()).thenReturn(false);
        AggregatedItem item1 = AggregatedItemHelper.map("abc", 123, "abd", 42);
        AggregatedItem item2 = AggregatedItemHelper.map("abc", 124, "abd", "qwe");
        Aggregated aggr = new Aggregated(item1, item2);

        printer.print(aggr);
        verify(console).printOut(nonColored("abc:$g123$n abd:$g42$n"));
        verify(console).printOut(nonColored("abc:$g124$n abd:$gqwe$n"));
    }

    @Test
    public void testPrintStatus() throws Exception {
        printer.printStatus("ABC", 1, "2", 3);
        verify(console).println("ABC", 1, "2", 3);
    }

    @Test
    public void testPrintStats() throws Exception {
        when(console.isTTY()).thenReturn(true);

        List hours = mock(List.class);
        List last = mock(List.class);
        List hosts = mock(List.class);
        List others = mock(List.class);
        Map fields = new LinkedHashMap();
        fields.put("host", hosts);
        fields.put("other", others);

        SearchStats stats = new SearchStats(hours, last, fields);

        when(bars.hours(hours, true)).thenReturn(Arrays.asList("aaa", "aaa", "aaa"));
        when(bars.lastHour(last, true)).thenReturn(Arrays.asList("bbb"));
        when(bars.field("host", hosts, true)).thenReturn(Arrays.asList("ccc"));
        when(bars.field("other", others, true)).thenReturn(Arrays.asList("ddd", "ddd"));

        printer.print(stats);

        verify(console, atLeastOnce()).isTTY();

        InOrder orderly = inOrder(console);
        orderly.verify(console).printOut("%s%s", "aaa", "bbb");
        orderly.verify(console).printOut("%s%s", "aaa", "");
        orderly.verify(console).printOut("%s%s", "aaa", "ccc");
        orderly.verify(console).printOut("%s%s", "", "");
        orderly.verify(console, times(2)).printOut("%s%s", "ddd", "");
        verifyNoMoreInteractions(console);
    }
}
