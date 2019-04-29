package com.xiaomakj.coroutines

import kotlinx.coroutines.CompletableDeferred

// 这里我们刚好使用sealed class来定义，定义一个CounterMsg
sealed class CounterMsg

// 定义一个用于自增的类型
object IncCounter : CounterMsg()

// 定义一个用户获取结果的类型(这里我们使用CompletableDeferred用于带回结果)
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg()
