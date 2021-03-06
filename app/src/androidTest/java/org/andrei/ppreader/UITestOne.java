package org.andrei.ppreader;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.andrei.ppreader.test.TestActivity1;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITestOne  {
    @Rule
   public ActivityTestRule<TestActivity1> activity1ActivityTestRule = new ActivityTestRule<>(TestActivity1.class);

    @Test
    public void testHelloWorldOnView() {

        final String text = "凉风习习，夜色迷离，轻纱般的薄雾缭绕着安静的县城。\n" +
                "\n" +
                "　　朦胧月光映照着清清的小河，河水从拱桥下缓缓流淌，岸边是鳞次栉比的两三层黑瓦小楼。水渍斑驳的墙面上，尽是青绿色的苔藓痕迹，还有些爬满了常青藤蔓，只露出开在临河一面的一溜窗户。\n" +
                "\n" +
                "　　此时已是三更半夜，除了河中的蛙声，巷尾的犬吠，再也听不到半分声音，只有东头一个窄小的窗洞里，透出昏黄的灯光，还有说话声隐隐传来……\n" +
                "\n" +
                "　　从敞开的窗户往里看，仅见一桌一凳一床，桌上点一盏黑乎乎的油灯，勉强照亮着三尺之间。长凳上搁一个缺个口的粗瓷碗，碗里盛着八九个罗汉豆子。一个身着破旧长袍，须发散乱，望之四十来岁的男人蹲在边上，一边照料着身前的小泥炉，一边与对面床上躺着的十几岁少年说话。\n" +
                "\n" +
                "　　他说一口带着吴侬腔调的官话，声音嘶哑道：“潮生啊，你且坚持一些，待为父煎好药，你服过便可痊愈了也。”\n" +
                "\n" +
                "　　床上那少年心中轻叹一声，暗道：‘这该是第三十遍念叨了吧？’但知道是为自己着急，也就不苛责他了。微微侧过头去，少年看到那张陌生而亲切的脸上，满是汗水和急切，心中顿感温暖。知道一时半会他也忙不完，便缓缓闭上眼睛，回想着近日来发生的不可思议。\n" +
                "\n" +
                "　　他本是一名年轻的副处长，正处在人生得意的阶段，却在一觉醒来，附身在这个奄奄一息的少年身上。并在少年神魂微弱之际，莫名其妙的与之融合，获得了这少年的意识和记忆，成为了这个五百年前的少年。\n" +
                "\n" +
                "　　是庄周还是蝴蝶？是原来的我还是现在的沈默？他已经完全糊涂了，似乎即是又是，似乎既不是也不是，或者说已经是一个全新的沈默了吧。\n" +
                "\n" +
                "　　事情就是这样荒诞，然而却确实发生，让他好几天无法面对，但后来转念一想，反正自己是个未婚的孤儿，无牵无挂，在哪里不是讨生活？再说用原先的副处级，换了这年青十好几岁的身体，似乎还是赚到了。\n" +
                "\n" +
                "　　只是突然生出许多属于那少年的情感，这让他有些不适应。\n" +
                "\n" +
                "　　适者生存，所以一定要适应。沈默这样对自己说道。\n" +
                "\n" +
                "　　~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "\n" +
                "　　一旦放开心怀，接受了新身份，一些属于那少年的记忆便潮水般涌来。他知道自己叫沈默，乳名唤作潮生，十三岁。是大明朝绍兴府会稽县永昌坊沈贺的独子。\n" +
                "\n" +
                "　　要说这沈贺，出身绍兴大族沈家……的旁支，家境尚算小康，自幼在族学中开蒙，学问那是很好的。十八岁便接连考中县试、府试、院试，成为一名每月领取廪米的廪生……廪生就是秀才，但秀才却不一定是廪生，因为只有考取一等的寥寥数人能得到国家奉养。\n" +
                "\n" +
                "　　能靠上这吃皇粮的秀才，沈贺很是给爹娘挣了脸面。\n" +
                "\n" +
                "　　然而时运倒转、造化弄人，沈相公从十九岁第一次参加秋闱开始，接连四次落第，这是很正常的事情，因为江浙一带乃是人文荟萃之地，绍兴府又拔尽江南文脉。余姚、会稽、山阴等几个县几乎家家小儿读书，可谓是藏龙卧虎，每年都有大批极优秀的读书人应举。\n" +
                "\n" +
                "　　名额有限、竞争残酷。像沈相公这样的，在别处早就中举了，可在绍兴这地方，却只能年复一年成为别人的陪衬。后来父母相继过世，他又连着守孝五年，等重新出来考试的时候，已经三十好几，应试最好的年纪也就过去了……\n" +
                "\n" +
                "　　可沈秀才这辈子就读书去了，不考试又能作甚？他不甘心失败，便又考了两届，结果不言而喻……空把的大好光阴都不说，还把颇为殷实的家底败了个干干净净，日子过的极为艰难，经年吃糠咽菜，见不到一点荤腥。\n" +
                "\n" +
                "　　去年夏天，沈秀才的媳妇中了暑气，积弱的身子骨竟一下子垮了。为了给媳妇看病，他连原来住的三进深的宅子都典卖了。结果人家欺他用急，将个价值百两的宅子，硬生生压到四十两，沈秀才书生气重，不齿于周借亲朋，竟真的咬牙卖掉了房产，在偏远巷里赁一栋廉价小楼，将老婆孩子安顿住下，给媳妇延医问药。\n" +
                "\n" +
                "　　结果银钱流水般的花出去，沈默******病却越来越重，到秋里卧床不起，至年前终于阖然而逝。沈贺用剩下的钱葬了妻子，却发现连最便宜的小楼都租不起了，爷俩只好‘结庐而居’。\n" +
                "\n" +
                "　　当然这是沈相公的斯文说法，实际上就是以竹木为屋架，以草苫覆盖遮拦，搭了个一间到底的草舍。虽然狭窄潮湿，但总算有个窝了不是？\n" +
                "\n" +
                "　　这时一家人唯一的收入来源，便是县学发的廪米，每月六斗。按说省着点，勉强也能凑合，但‘半大小子，饿死老子’，沈默正是长身体的时候，食量比他爹还大，这点粳米哪能足够？沈秀才只得去粮铺换成最差的籼米，这样可以得到九斗。沈默再去乡间挖些野菜、捉些泥鳅回来，这才能刚刚对付两人的膳食。\n" +
                "\n" +
                "　　~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "\n" +
                "　　俗话说祸不单行，一点也不假，几天前沈默去山上挖野菜，竟然被条受惊的毒蛇给咬了小腿，被同去的哥儿几个送回来时，已经是满脸黑气，眼看就要不行了。\n" +
                "\n" +
                "　　后来发生的事情，沈默就不知道了。当他悠悠醒来，便发现自己已经置身于一间阁楼之中。虽然檩柱屋顶间挂满了蜘蛛落网，空气中还弥散着一股腐朽酸臭的味道，却比那透风漏雨、阴暗潮湿的草棚子要强很多。\n" +
                "\n" +
                "　　正望着一只努力吐丝的蜘蛛出神，沈默听……父亲道：“好了好了，潮生吃药了。”便被扶了起来。他上身靠在枕头上，端量着今后称之为父的男人，只见他须发蓬乱，脸色青白，眼角已经有了皱纹，嘴角似乎有些青淤，颧骨上亦有些新鲜的伤痕。身上的长袍也是又脏又破，仿佛跟人衅过架，还不出意料输了的样子。\n" +
                "\n" +
                "　　见沈默睁眼看自己，沈贺的双目中满是兴奋和喜悦，激动道：“得好生谢谢殷家小姐，若没得她出手相救，咱爷俩就得阴阳永隔了……”说着便眼圈一红，啪嗒啪嗒掉下泪来。\n" +
                "\n" +
                "　　看到他哭，沈默的鼻头也有些发酸，想要开口安慰一下，喉咙却仿佛加了塞子一般，一个字也说不出来。\n" +
                "\n" +
                "　　注意到他表情的变化，沈贺赶紧擦擦泪道：“怎么了，你哪里不舒服吗？”见沈默看向药碗，沈贺不好意思道：“险些忘记了。”便端起碗来，舀一勺褐色的汤药，先在嘴边吹几下，再小心的搁到他嘴边。\n" +
                "\n" +
                "　　沈默皱着眉头轻啜一口，却没有想象中那么苦涩，反倒有些苦中带甜。见他眉头舒缓下来，沈贺高兴道：“你从小不爱吃药，我买了些杏花蜜掺进去，大夫说有助于你复原的。”便伺候着他将一碗药喝下去。\n" +
                "\n" +
                "　　~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                "\n" +
                "　　用毛巾给沈默擦擦嘴，再把他重新放躺，沈贺很有成就感的长舒口气，仿佛做完一件大事一般。这才直起身，将空药碗和破碗搁到桌上，一屁股坐在凳子上，疲惫的弯下腰，重重喘一口粗气。\n" +
                "\n" +
                "　　沈默见他盛满一碗开水，从破碗中捻起三粒青黄色的蚕豆，稍一犹豫，又将手一抖，将其中两粒落回碗中，仅余下一颗捏在手中。\n" +
                "\n" +
                "　　端详那一粒豆子许久，沈贺闭上眼，将其缓缓送入口中，慢慢咀嚼起来，动作极是轻柔，仿佛在回味无穷，久久不能自拔。\n" +
                "\n" +
                "　　良久，沈贺才缓缓睁开眼，微微摇头赋诗道：“曹娥运来芽青豆，谦裕同兴好酱油；东关请来好煮手，吃到嘴里糯柔柔。”\n" +
                "\n" +
                "　　沈默汗颜，他从来不知道，原来吃一个豆也会引起这么大的幸福感。\n" +
                "\n" +
                "　　见他流露出不以为然的神情，沈贺轻抿一口开水道：“潮生，你是没有尝到啊，这\n" +
                "\n" +
                "　　豆肉熟而不腐、软而不烂，咀嚼起来满口生津，五香馥郁，又咸而透鲜，回味微甘……若能以黄酒佐之，怕是土地公公都要来尝一尝的。”\n" +
                "\n" +
                "　　‘土地公就没吃过点好东西？’沈默翻翻白眼，却被沈贺以为在抱怨他吃独食，连忙解释道：“不是为父不与你分享，而是大夫嘱咐过，你不能食用冷热酸硬的东西，还是等痊愈了再说吧。”\n" +
                "\n" +
                "　　沈默无力的点点头，见沈贺又用同样的速度吃掉两颗，便将手指在抹布上揩了楷，把一碗水都喝下去，一脸满足道：“晚饭用过，咱爷俩该睡觉了。”\n" +
                "\n" +
                "　　沈默的眼睛瞪得溜圆，沈贺一本正经道：“圣人云：‘事不过三’，这第一次吃叫品尝，第二次叫享受，第三次叫充饥，再多吃就是饕餮浪费了。”说着朝他挤眼笑笑道：“睡吧。”便吹熄油灯，趴在桌子上睡了。\n" +
                "\n" +
                "　　因为这屋里只有一张单人床……\n" +
                "\n" +
                "　　－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－分割－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－\n" +
                "\n" +
                "　　没有别的要说的，只有一句话，让我们开始一段美好的回忆吧，亲爱的们，let‘sgo!!";

        final PPReaderPageManager mgr = new PPReaderPageManager();
        final TestActivity1 activity1 = activity1ActivityTestRule.getActivity();
       // final TextView ttv = (TextView)activity1.findViewById(R.id.novel_reader_text);
        //assertEquals(1,activity1.m_val);
        final Instrumentation r =  InstrumentationRegistry.getInstrumentation();
        r.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                TextView ttv = new TextView(r.getTargetContext());
                ttv.setWidth(720);
                ttv.setHeight(1080);
                ttv.setText("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
                int count = ttv.getLineCount();
               // mgr.injectText(0,ttv.getLayout());
            }
        });

        r.waitForIdleSync();
        assertEquals(5,mgr.getCount());

    }




}

