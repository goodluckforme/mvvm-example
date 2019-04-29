package com.xiaomakj.coroutines

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import thereisnospon.codeview.CodeViewTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {
    var sb: StringBuffer = StringBuffer("")

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            R.id.menu_clear == item.itemId -> {
                result.text = ""
                delayTestResult.text = ""
                delayTest2Result.text = ""
                runBlockingResult.text = ""
                asyncTestResult.text = ""
                jobTestResult.text = ""
                safeTestResult.text = ""
                actorTestResult.text = ""
                channelTestResult.text = ""
                producerTestResult.text = ""
                pipelineTestResult.text = ""
                selectTestResult.text = ""
                return true
            }
            R.id.menu_yuanma == item.itemId -> {
                "https://github.com/goodluckforme/mvvm-example".onClickCopy(this@MainActivity)
                return true
            }
            R.id.menu_share == item.itemId -> {
                "https://blog.dreamtobe.cn/kotlin-coroutines/".onClickCopy(this@MainActivity)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getString(R.string.simple).let {
            codeview_simple.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_simple.showCode(it)
        }
        getString(R.string.delayTest).let {
            codeview_delayTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_delayTest.showCode(it)
        }
        getString(R.string.delayTest2).let {
            codeview_delayTest2.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_delayTest2.showCode(it)
        }
        getString(R.string.runblock).let {
            codeview_runblock.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_runblock.showCode(it)
        }
        getString(R.string.asyncTest).let {
            codeview_asyncTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_asyncTest.showCode(it)
        }
        getString(R.string.jobTest).let {
            codeview_jobTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_jobTest.showCode(it)
        }
        getString(R.string.safeTest).let {
            codeview_safeTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_safeTest.showCode(it)
        }
        getString(R.string.actorTest).let {
            codeview_actorTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_actorTest.showCode(it)
        }
        getString(R.string.channelTest).let {
            codeview_channelTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_channelTest.showCode(it)
        }
        getString(R.string.producerTest).let {
            codeview_producerTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_producerTest.showCode(it)
        }
        getString(R.string.pipelineTest).let {
            codeview_pipelineTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_pipelineTest.showCode(it)
        }
        getString(R.string.selectTest).let {
            codeview_selectTest.setTheme(CodeViewTheme.ANDROIDSTUDIO).fillColor().onClickCopy(it)
            codeview_selectTest.showCode(it)
        }

        simpleTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    result.text = sb.toString()
                }
                simpleTest()
            }
        }
        delayTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    delayTestResult.text = sb.toString()
                }
                delayTest()
            }
        }
        delayTest2.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    delayTest2Result.text = sb.toString()
                }
                delayTest2()
            }
        }
        runBlockingTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    runBlockingResult.text = sb.toString()
                }
                runBlockingTest()
            }
        }
        asyncTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    asyncTestResult.text = sb.toString()
                }
                asyncTest()
            }
        }
        jobTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    asyncTestResult.text = sb.toString()
                }
                jobTest()
            }
        }
        safeTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    safeTestResult.text = sb.toString()
                }
                //线程安全
                //在CommonPool线程池中执行coutner自增
                massiveRun(safeTestResult, Dispatchers.IO) {
                    //每次我们都自增一次coutiner
                    //方式三
                    //synchronized(lock) {
                    //    counter++
                    //}
                    //方式二
                    //mutex.lock()
                    //try { counter++ }
                    //finally { mutex.unlock() }
                    counter.incrementAndGet()
                }
                safeTestResult.addString("Counter = $counter\n", sb)
            }
        }
        actorTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    actorTestResult.text = sb.toString()
                }
                actor()
            }
        }
        channelTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    channelTestResult.text = sb.toString()
                }
                channelTest()
            }
        }
        producerTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    producerTestResult.text = sb.toString()
                }
                producerTest()
            }
        }
        pipelineTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    pipelineTestResult.text = sb.toString()
                }
                pipelineTest()
            }
        }
        selectTest.setOnClickListener {
            progressbar.canLoad {
                resetSb { sb ->
                    this@MainActivity.sb = sb
                    selectTestResult.text = sb.toString()
                }
                selectTest()
            }
        }

    }

    fun selectTest() = runBlocking {
        // 每300ms发送一个channel1
        fun channel1(context: CoroutineContext) = produce<String>(context) {
            while (true) {
                delay(300)
                send("channel1")
            }
        }

        // 每100ms发送一个channel2
        fun channel2(context: CoroutineContext) = produce<String>(context) {
            while (true) {
                delay(100)
                send("channel2")
            }
        }

        // 每次选择先到达的一个
        suspend fun selectFirstChannel(channel1: ReceiveChannel<String>, channel2: ReceiveChannel<String>) {
            select<Unit> {
                // 这里的<Unit>说明这个select没有产生任何返回值
                channel1.onReceive { value ->
                    selectTestResult.addString("$value \n",sb)
                }
                channel2.onReceive { value ->
                    selectTestResult.addString("$value \n",sb)
                }
            }
        }

        val channel1 = channel1(coroutineContext)
        val channel2 = channel2(coroutineContext)

        repeat(5) {
            selectFirstChannel(channel1, channel2)
        }

        channel1.cancel()
        channel2.cancel()
        progressbar.hide()
    }

    fun pipelineTest() = runBlocking<Unit> {
        // 创建一个生产者，返回的是一个ProducerJob
        fun produceNumbers() = produce<Int> {
            var x = 1
            while (true) send(x++) // infinite stream of integers starting from 1
        }

        // 创建一个用于加工生产者的生产者（ProducerJob是继承自ReceiveChannel)
        fun square(numbers: ReceiveChannel<Int>) = produce<Int> {
            for (x in numbers) send(x * x)
        }

        val numbers = produceNumbers() // 生产者
        val squares = square(numbers) // 加工
        for (i in 1..5) pipelineTestResult.addString("${squares.receive()}\n", sb) // 消费前5个结果
        // cancel加工的coroutine（一般来说是不用主动cancel的，因为协程就好像一个常驻线程，
        // 挂起也会被其他任务使用闲置资源，不过大型应用推荐cancel不使用的coroutine)
        squares.cancel()
        // cancel生产者的coroutine
        numbers.cancel()

        progressbar.hide()
    }

    fun producerTest() = runBlocking<Unit> {
        // 创建一个生产者方法
        // 得到生产者
        val squares = produce<Int> {
            for (x in 1..5) send(x * x)
        }
        // 对生产者生产的每一个结果进行消费
        squares.consumeEach {
            producerTestResult.addString("$it\n", sb)
        }
        progressbar.hide()
    }

    suspend fun player(name: String, table: Channel<Ball>) {
        for (ball in table) { // 不断接球
            ball.hits++
            channelTestResult.addString("$name $ball\n", sb)
            delay(300) // 等待300ms
            table.send(ball) // 发球
        }
    }

    fun channelTest() = runBlocking {
        val table = Channel<Ball>() // 创建一个channel作为桌子
        launch(coroutineContext) { player("ping", table) } // 选手一，先接球中
        launch(coroutineContext) { player("pong", table) } // 选手二，也开始接球
        launch(coroutineContext) { player("kong", table) } // 选手二，也开始接球
        table.send(Ball(0)) // 开球，发出第一个球
        delay(5000) // 打一秒钟
        table.receive() // 接球，终止在player中的循环发球
        table.close()
        progressbar.hide()
    }

    fun actor() = runBlocking  {
        // 这个方法启动一个新的Counter Actor
        // 创建一个Actor
        val counter = actor<CounterMsg>(Dispatchers.IO) {
            var counter = 0
            for (msg in channel) { // 不断接收channel中的数据，这个channel是ActorScope的变量
                when (msg) {
                    is IncCounter -> counter++ // 如果是IncCounter类型，我们就自增
                    is GetCounter -> msg.response.complete(counter) // 如果是GetCounter类型，我们就带回结果
                }
            }
        }

        massiveRun(actorTestResult, Dispatchers.IO) {
            counter.send(IncCounter) // action发送自增类型，使得不断执行action不断的触发自增
        }

        // 创建一个CompletableDeferred用于带回结果
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response)) // 发送GetCounter类型带回结果
        actorTestResult.addString("Counter = ${response.await()}", sb) // 输出结果
        counter.close() // 关闭actor
    }

    //    counter 的初始值
//    @Volatile
//    var counter = 0
    //使用原子级别
    private val counter = AtomicInteger()
    //效率太低
    private val lock = Any()
    //优化十秒
    val mutex = Mutex()

    fun massiveRun(textView: TextView, context: CoroutineContext, action: suspend () -> Unit) = runBlocking {
        val n = 1000 // launch的个数
        val k = 1000 // 每个coroutine中执行action的次数
        val time = measureTimeMillis {
            val jobs = List(n) {
                launch(context) {
                    repeat(k) {
                        action()
                    }
                }
            }
            jobs.forEach {
                it.join()
            }
        }
        textView.addString("Completed ${n * k} actions in $time ms\n", sb)
        progressbar.hide()
    }

    // Only the original thread that created a view hierarchy can touch its views.
    fun delayTest() = runBlocking {
        // 使用协程
        delayTestResult.addString("Coroutines: start\n", sb)
        val jobs = List(1000) {
            // 创建新的coroutine
            GlobalScope.launch {
                // 挂起当前上下文而非阻塞1000ms
                delay(1000L)
                delayTestResult.addString("." + Thread.currentThread().name + "\n", sb)
                //Log.i("delayTest", "." + Thread.currentThread().name)
            }
        }
        jobs.forEach { it.join() }
        delayTestResult.addString("Coroutines: end\n", sb)
        progressbar.hide()
    }

    fun delayTest2() {
        val noCoroutinesPool: ExecutorService = Executors.newCachedThreadPool()
        delayTest2Result.addString("No Coroutines: start", sb)
// 使用阻塞
        val noCoroutinesJobs = List(1000) {
            Executors.callable {
                Thread.sleep(1000L)
                delayTest2Result.addString("thread." + Thread.currentThread().name, sb)
            }
        }
        noCoroutinesPool.invokeAll(noCoroutinesJobs)
        delayTest2Result.addString("No Coroutines: end", sb)
        progressbar.hide()
    }

    private fun simpleTest() = GlobalScope.launch(Dispatchers.Main) {
        async(Dispatchers.IO) {
            result.addString("执行等待 5S前 \n", sb)
            delay(2000)
            //延迟5秒后下面的不执行
            result.addString("执行等待 5S后 \n", sb)
        }.await()
        //没有await 会直接向下执行
        result.addString("等待异步线程执行结束", sb)
        progressbar.hide()
    }

    //解决 join报错 ：Suspend function 'join' should be called only from a coroutine or another suspend function
    fun runBlockingTest() = runBlocking<Unit> {
        val job = launch {
            // 挂起5000ms  界面卡主5秒
            delay(5000L)
        }
        // 接口含义同Thread.join只是这里是`suspension`
        job.join()
        progressbar.hide()
        runBlockingResult.addString("界面卡主5秒", sb)
    }

    fun asyncTest() = runBlocking {
        // 计算总共需要执行多久，measureTimeMillis是kotlin标准库中所提供的方法
        val time = measureTimeMillis {
            val one = async(Dispatchers.IO) {
                asyncTestResult.addString("doOne\n", sb)
                doOne()
            } // 这里将doOne抛到CommonPool中的线程执行，并在结束时将结果带回来。
            val two = async(Dispatchers.IO) {
                asyncTestResult.addString("doTwo\n", sb)
                doTwo()
            } // 这里将doTwo抛到CommonPool中的线程执行，并在结束时将结果带回来。
            asyncTestResult.addString("The answer is ${one.await() + two.await()}", sb) // 这里会输出6
        }
        asyncTestResult.addString("\t总耗时${time}ms\n", sb) // 由于doOne与doTwo在异步执行，因此这里输出大概是700ms
        progressbar.hide()

        // 这里doOne将不会立马执行
        val one = async(Dispatchers.IO, CoroutineStart.LAZY) {
            //最后一行为返回值
            asyncTestResult.addString("doOne2 = " + doOne(), sb)
        }
        // 此时将会挂起当前上下文等待doOne执行完成，然后输出返回值
        asyncTestResult.addString("${one.await()}", sb)
    }

    fun jobTest() = runBlocking {
        // 创建一个运行在CommonPool线程池中的Coroutine
        val request = launch() {
            // 创建一个运行在CommonPool线程池中的coroutine
            val job1 = launch {
                jobTestResult.addString("job1: I have my own context and execute independently!\n", sb)
                delay(1000)
                jobTestResult.addString("job1: I am not affected by cancellation of the request\n", sb)
            }
            // 创建一个运行在父CoroutineContext上的coroutine
            val job2 = launch(coroutineContext) {
                jobTestResult.addString("job2: I am a child of the request coroutine\n", sb)
                delay(1000)
                jobTestResult.addString("job2: I will not execute this line if my parent request is cancelled\n", sb)
            }

        }
        //只有等上面执行完才能执行下面
        //request.join()
        delay(500)
        request.cancel() // 取消
        delay(1000) // delay a second to see what happens
        jobTestResult.addString("main: Who has survived request cancellation?\n", sb)
        progressbar.hide()
    }
}