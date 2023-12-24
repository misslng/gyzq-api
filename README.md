# gyzq-api
国元证券交易协议APi
加密的算法使用了SM4国密加密。CV了出来，都可以 正常使用了。

目前只完成了登录功能，懒==

如果需要开发其他功能，请下载国元点金 ver7.0.6
使用Fiddler康康获取持仓，下单等其他操作是怎么发送请求的,然后解密一下康康文本是怎么样的,具体参考我代码里的登录,都差不多.

![avatar](1.png)


2023/11/2
寄掉了,好快的修

2023/11/4

改成socket通信了7.0.10之后的版本

com.android.dazhihui.b.b.n

com.android.dazhihui.b.b.c

感兴趣可以分析一下这两个函数

2023/12/24

周末回家 闲着无聊小看了一下

发送数据转到了native层 具体还没看 可能wireshark对比下native还有没有啥操作 没有的话要写代码直接Java写完了

登录到交易这里 最后的数据还是使用sm4加密 组字符串的地方是在com.android.dazhihui.ui.b.b.e 的h函数

然后SM4加密 的密钥是动态的 可能是之前那个A0协议AE协议返回的 不太清楚 IV的话是根据SYNID来的

贴上加密处理地方的frida代码

```
           let DataBody = Java.use("com.android.dazhihui.ui.b.b.l");
            DataBody["a"].overload('[Lcom.android.dazhihui.ui.b.b.l;', 'com.android.dazhihui.b.b.m').implementation = function (dataBodyArr, mVar) {
                // console.log(`DataBody.a is called: dataBodyArr=${dataBodyArr}, mVar=${mVar}`);
                console.log("AES KEY=" + this.KEY);
                for (const data of dataBodyArr) {
                    console.log("未加密数据=" + data.data.value);
                    console.log("数据类型=" + data.type.value);
                }
                let result = this["a"](dataBodyArr, mVar);
                console.log(`返回结果=${result}`);
                return result;
            };
            let g = Java.use("com.android.dazhihui.ui.b.b.c.g");
            g["c"].implementation = function (bArr) {
                // console.log(`g.c is called: bArr=${bArr}`);
                console.log("SM4加密 密钥=" + this.secretKey.value + "  IV=" + this.iv.value);
                let result = this["c"](bArr);
                // console.log(`g.c result=${result}`);
                return result;
            };
```
关注com.android.dazhihui.ui.b.b.l的

public static byte[] a(DataBody[] dataBodyArr, com.android.dazhihui.b.b.m mVar) 这是发送包的处理

public static DataBody[] a(byte[] bArr) 这个应该是接收的 还没看

DataBody是我自己的命名 大概一下= =

在com.android.dazhihui.ui.b.a的handleResponse应该就是登录的执行顺序了 不知道D协议是个啥 有大佬知道麻烦指教一下(跪)

整体的流程应该是AE协议-》A0协议跟服务器协商出SM4的加密密钥 然后之后的数据就根据这个密钥来加密 我猜的

接收的处理地方 应该就是在com.android.dazhihui.b.g的mDelegateConnect的receiveData 接收解密完之后用sendMessage发给其他的handler

版本是国元证券的7.1.0版本 感兴趣的小伙伴可以看一下
