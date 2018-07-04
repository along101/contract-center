<template>
    <div>
        <div class="markdown-preview-plus-view" data-use-github-style>
        <div>
            <h2>Proto 语法指南 – raptor版本</h2>
            <h3>Raptor 自定义注解介绍</h3>
            <p>Raptor 自定义了一些注解, 用来记录一些元信息,使 raptor-codegen 生成定制程度比较高的代码.</p>
            <p>对此,我们提供了一个<a href="/#/appSummary?appId=contract-center-demo">例子</a>.</p>
            <h4>FileOptions</h4>
            <p>FileOptions 用来定义整个proto一些信息,他们包括:</p>
            <ul>
                <li>appId – proto文件所在应用的appId</li>
                <li>appName – proto文件所在引用的应用名</li>
                <li>version – proto文件的版本号(和契约中心的版本概念无关)Â</li>
                <li>summary – proto文件的概要说明</li>
            </ul>
            <p>在契约中心中,appId是必填项.appId必须和proto文件所在的应用匹配,否则proto文件无法发布.</p>
            <p>appId和appName可以作为寻找服务配置的key具体见 [Raptor客户端配置].</p>
            <h4>ServiceOptions</h4>
            <p>目前ServiceOptions就一个: <strong>servicePath</strong>.这个option用于定义该service公共的url前缀,类似于在Contoller类上的@RequestMapping注解.</p>
            <h4>MethodOptions</h4>
            <p>MethodOption 是定义在每一个rpc方法中的option,他们包括:</p>
            <ul>
                <li>method – 指定调用该rpc的http method</li>
                <li>path – 指定方法的url,类似于在Contoller类方法上的@RequestMapping注</li>
            </ul>
        </div>

        <div class="article-entry" itemprop="articleBody">

            <h1 id="expanderHead" style="cursor:pointer;">
                目录 <span id="expanderSign">[−]</span>
            </h1>
            <div id="article-entry-toc" data-role="collapsible" class="article-entry-toc" style="display: block;">
                <ol class="toc">
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#定义一个消息类型"><span class="toc-text">定义一个消息类型</span></a>
                        <ol class="toc-child">
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#指定字段类型"><span
                                    class="toc-text">指定字段类型</span></a></li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#分配标识号"><span
                                    class="toc-text">分配标识号</span></a></li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#指定字段规则"><span
                                    class="toc-text">指定字段规则</span></a></li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#添加更多消息类型"><span class="toc-text">添加更多消息类型</span></a>
                            </li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#添加注释"><span class="toc-text">添加注释</span></a>
                            </li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#保留标识符（Reserved）"><span class="toc-text">保留标识符（Reserved）</span></a>
                            </li>
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#从-proto文件生成了什么？"><span class="toc-text">从.proto文件生成了什么？</span></a>
                            </li>
                        </ol>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#标量数值类型"><span class="toc-text">标量数值类型</span></a>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#默认值"><span class="toc-text">默认值</span></a></li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#枚举"><span class="toc-text">枚举</span></a></li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#使用其他消息类型"><span class="toc-text">使用其他消息类型</span></a>
                        <ol class="toc-child">
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#导入定义"><span class="toc-text">导入定义</span></a>
                            </li>
                        </ol>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#嵌套类型"><span class="toc-text">嵌套类型</span></a>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#更新一个消息类型"><span class="toc-text">更新一个消息类型</span></a>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#Map"><span class="toc-text">Map</span></a>
                        <ol class="toc-child">
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#向后兼容性问题-1"><span class="toc-text">向后兼容性问题</span></a>
                            </li>
                        </ol>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#Package"><span
                            class="toc-text">Package</span></a>
                        <ol class="toc-child">
                            <li class="toc-item toc-level-3"><a class="toc-link" href="#包及名称的解析"><span
                                    class="toc-text">包及名称的解析</span></a></li>
                        </ol>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#定义服务(Service)"><span class="toc-text">定义服务(Service)</span></a>
                    </li>
                    <li class="toc-item toc-level-2"><a class="toc-link" href="#JSON_映射"><span
                            class="toc-text">JSON 映射</span></a></li>

                </ol>
            </div>


            <h2 id="定义一个消息类型">定义一个消息类型</h2>
            <p>先来看一个非常简单的例子。假设你想定义一个“搜索请求”的消息格式，每一个请求含有一个查询字符串、你感兴趣的查询结果所在的页数，以及每一页多少条查询结果。可以采用如下的方式来定义消息类型的.proto文件了：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line">syntax = <span class="string">"proto3"</span>;</div><div class="line"></div><div
                            class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchRequest</span> </span>{</div><div
                            class="line">  <span class="built_in">string</span> query = <span
                            class="number">1</span>;</div><div class="line">  <span class="built_in">int32</span> page_number = <span
                            class="number">2</span>;</div><div class="line">  <span class="built_in">int32</span> result_per_page = <span
                            class="number">3</span>;</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <ul>
                <li>文件的第一行指定了你正在使用proto3语法：如果你没有指定这个，编译器会使用proto2。这个指定语法行必须是文件的非空非注释的第一个行。</li>
                <li>SearchRequest消息格式有3个字段，在消息中承载的数据分别对应于每一个字段。其中每个字段都有一个名字和一种类型。</li>
            </ul>
            <h3 id="指定字段类型">指定字段类型</h3>
            <p>
                在上面的例子中，所有字段都是标量类型：两个整型（page_number和result_per_page），一个string类型（query）。当然，你也可以为字段指定其他的合成类型，包括枚举（enumerations）或其他消息类型。</p>
            <h3 id="分配标识号">分配标识号</h3>
            <p>
                正如你所见，在消息定义中，每个字段都有唯一的一个数字标识符。这些标识符是用来在消息的二进制格式中识别各个字段的，一旦开始使用就不能够再改变。注：[1,15]之内的标识号在编码的时候会占用一个字节。[16,2047]之内的标识号则占用2个字节。所以应该为那些频繁出现的消息元素保留
                [1,15]之内的标识号。切记：要为将来有可能添加的、频繁出现的标识号预留一些标识号。</p>
            <p>最小的标识号可以从1开始，最大到2^29 - 1, or 536,870,911。不可以使用其中的[19000－19999]（ (从FieldDescriptor::kFirstReservedNumber 到
                FieldDescriptor::kLastReservedNumber)）的标识号，
                Protobuf协议实现中对这些进行了预留。如果非要在.proto文件中使用这些预留标识号，编译时就会报警。同样你也不能使用早期保留的标识号。</p>
            <h3 id="指定字段规则">指定字段规则</h3>
            <p>所指定的消息字段修饰符必须是如下之一：</p>
            <ul>
                <li>singular：一个格式良好的消息应该有0个或者1个这种字段（但是不能超过1个）。</li>
                <li>repeated：在一个格式良好的消息中，这种字段可以重复任意多次（包括0次）。重复的值的顺序会被保留。</li>
            </ul>
            <p>在proto3中，repeated的标量域默认情况虾使用packed。</p>
            <p>你可以了解更多的pakced属性在<a href="https://developers.google.com/protocol-buffers/docs/encoding?hl=zh-cn#packed"
                                   target="_blank" rel="external">Protocol Buffer 编码</a></p>
            <h3 id="添加更多消息类型">添加更多消息类型</h3>
            <p>在一个.proto文件中可以定义多个消息类型。在定义多个相关的消息的时候，这一点特别有用——例如，如果想定义与SearchResponse消息类型对应的回复消息格式的话，你可以将它添加到相同的.proto文件中，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div><div class="line">9</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchRequest</span> </span>{</div><div
                            class="line">  <span class="built_in">string</span> query = <span
                            class="number">1</span>;</div><div class="line">  <span class="built_in">int32</span> page_number = <span
                            class="number">2</span>;</div><div class="line">  <span class="built_in">int32</span> result_per_page = <span
                            class="number">3</span>;</div><div class="line">}</div><div class="line"></div><div
                            class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchResponse</span> </span>{</div><div
                            class="line"> ...</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h3 id="添加注释">添加注释</h3>
            <p>向.proto文件添加注释，可以使用C/C++/Java风格的双斜杠（//） 语法格式，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchRequest</span> </span>{</div><div
                            class="line">  <span class="built_in">string</span> query = <span
                            class="number">1</span>;</div><div class="line">  <span class="built_in">int32</span> page_number = <span
                            class="number">2</span>;  <span
                            class="comment">// Which page number do we want?</span></div><div class="line">  <span
                            class="built_in">int32</span> result_per_page = <span class="number">3</span>;  <span
                            class="comment">// Number of results to return per page.</span></div><div
                            class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h3 id="保留标识符（Reserved）">保留标识符（Reserved）</h3>
            <p>
                如果你通过删除或者注释所有域，以后的用户在更新这个类型的时候可能重用这些标识号。如果你使用旧版本加载相同的.proto文件会导致严重的问题，包括数据损坏、隐私错误等等。现在有一种确保不会发生这种情况的方法就是为字段tag（reserved
                name可能会JSON序列化的问题）指定<code>reserved</code>标识符，protocol buffer的编译器会警告未来尝试使用这些域标识符的用户。</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">Foo</span> </span>{</div><div
                            class="line">  reserved <span class="number">2</span>, <span class="number">15</span>, <span
                            class="number">9</span> to <span class="number">11</span>;</div><div class="line">  reserved <span
                            class="string">"foo"</span>, <span class="string">"bar"</span>;</div><div
                            class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>注：不要在同一行reserved声明中同时声明域名字和tag number。</p>
            <h3 id="从-proto文件生成了什么？">从.proto文件生成了什么？</h3>
            <p>当用protocol
                buffer编译器来运行.proto文件时，编译器将生成所选择语言的代码，这些代码可以操作在.proto文件中定义的消息类型，包括获取、设置字段值，将消息序列化到一个输出流中，以及从一个输入流中解析消息。</p>
            <ul>
                <li>对C++来说，编译器会为每个.proto文件生成一个.h文件和一个.cc文件，.proto文件中的每一个消息有一个对应的类。</li>
                <li>对Java来说，编译器为每一个消息类型生成了一个.java文件，以及一个特殊的Builder类（该类是用来创建消息类接口的）。</li>
                <li>
                    对Python来说，有点不太一样——Python编译器为.proto文件中的每个消息类型生成一个含有静态描述符的模块，，该模块与一个元类（metaclass）在运行时（runtime）被用来创建所需的Python数据访问类。
                </li>
                <li>对go来说，编译器会位每个消息类型生成了一个.pd.go文件。</li>
                <li>对于Ruby来说，编译器会为每个消息类型生成了一个.rb文件。</li>
                <li>javaNano来说，编译器输出类似域java但是没有Builder类</li>
                <li>对于Objective-C来说，编译器会为每个消息类型生成了一个pbobjc.h文件和pbobjcm文件，.proto文件中的每一个消息有一个对应的类。</li>
                <li>对于C#来说，编译器会为每个消息类型生成了一个.cs文件，.proto文件中的每一个消息有一个对应的类。<br>你可以从如下的文档链接中获取每种语言更多API(proto3版本的内容很快就公布)。<a
                        href="https://developers.google.com/protocol-buffers/docs/reference/overview" target="_blank"
                        rel="external">API Reference</a></li>
            </ul>
            <h2 id="标量数值类型">标量数值类型</h2>
            <p>一个标量消息字段可以含有一个如下的类型——该表格展示了定义于.proto文件中的类型，以及与之对应的、在自动生成的访问类中定义的类型：</p>
            <table>
                <thead>
                <tr>
                    <th>.proto Type</th>
                    <th align="center">Notes</th>
                    <th align="right">C++ Type</th>
                    <th>Java Type</th>
                    <th>Python Type[2]</th>
                    <th>Go Type</th>
                    <th>Ruby Type</th>
                    <th>C# Type</th>
                    <th>PHP Type</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>double</td>
                    <td align="center"></td>
                    <td align="right">double</td>
                    <td>double</td>
                    <td>float</td>
                    <td>float64</td>
                    <td>Float</td>
                    <td>double</td>
                    <td>float</td>
                </tr>
                <tr>
                    <td>float</td>
                    <td align="center"></td>
                    <td align="right">float</td>
                    <td>float</td>
                    <td>float</td>
                    <td>float32</td>
                    <td>Float</td>
                    <td>float</td>
                    <td>float</td>
                </tr>
                <tr>
                    <td>int32</td>
                    <td align="center">使用变长编码，对于负值的效率很低，如果你的域有可能有负值，请使用sint64替代</td>
                    <td align="right">int32</td>
                    <td>int</td>
                    <td>int</td>
                    <td>int32</td>
                    <td>Fixnum 或者 Bignum（根据需要）</td>
                    <td>int</td>
                    <td>integer</td>
                </tr>
                <tr>
                    <td>uint32</td>
                    <td align="center">使用变长编码</td>
                    <td align="right">uint32</td>
                    <td>int</td>
                    <td>int/long</td>
                    <td>uint32</td>
                    <td>Fixnum 或者 Bignum（根据需要）</td>
                    <td>uint</td>
                    <td>integer</td>
                </tr>
                <tr>
                    <td>uint64</td>
                    <td align="center">使用变长编码</td>
                    <td align="right">uint64</td>
                    <td>long</td>
                    <td>int/long</td>
                    <td>uint64</td>
                    <td>Bignum</td>
                    <td>ulong</td>
                    <td>integer/string</td>
                </tr>
                <tr>
                    <td>sint32</td>
                    <td align="center">使用变长编码，这些编码在负值时比int32高效的多</td>
                    <td align="right">int32</td>
                    <td>int</td>
                    <td>int</td>
                    <td>int32</td>
                    <td>Fixnum 或者 Bignum（根据需要）</td>
                    <td>int</td>
                    <td>integer</td>
                </tr>
                <tr>
                    <td>sint64</td>
                    <td align="center">使用变长编码，有符号的整型值。编码时比通常的int64高效。</td>
                    <td align="right">int64</td>
                    <td>long</td>
                    <td>int/long</td>
                    <td>int64</td>
                    <td>Bignum</td>
                    <td>long</td>
                    <td>integer/string</td>
                </tr>
                <tr>
                    <td>fixed32</td>
                    <td align="center">总是4个字节，如果数值总是比总是比228大的话，这个类型会比uint32高效。</td>
                    <td align="right">uint32</td>
                    <td>int</td>
                    <td>int</td>
                    <td>uint32</td>
                    <td>Fixnum 或者 Bignum（根据需要）</td>
                    <td>uint</td>
                    <td>integer</td>
                </tr>
                <tr>
                    <td>fixed64</td>
                    <td align="center">总是8个字节，如果数值总是比总是比256大的话，这个类型会比uint64高效。</td>
                    <td align="right">uint64</td>
                    <td>long</td>
                    <td>int/long</td>
                    <td>uint64</td>
                    <td>Bignum</td>
                    <td>ulong</td>
                    <td>integer/string</td>
                </tr>
                <tr>
                    <td>sfixed32</td>
                    <td align="center">总是4个字节</td>
                    <td align="right">int32</td>
                    <td>int</td>
                    <td>int</td>
                    <td>int32</td>
                    <td>Fixnum 或者 Bignum（根据需要）</td>
                    <td>int</td>
                    <td>integer</td>
                </tr>
                <tr>
                    <td>sfixed64</td>
                    <td align="center">总是8个字节</td>
                    <td align="right">int64</td>
                    <td>long</td>
                    <td>int/long</td>
                    <td>int64</td>
                    <td>Bignum</td>
                    <td>long</td>
                    <td>integer/string</td>
                </tr>
                <tr>
                    <td>bool</td>
                    <td align="center"></td>
                    <td align="right">bool</td>
                    <td>boolean</td>
                    <td>bool</td>
                    <td>bool</td>
                    <td>TrueClass/FalseClass</td>
                    <td>bool</td>
                    <td>boolean</td>
                </tr>
                <tr>
                    <td>string</td>
                    <td align="center">一个字符串必须是UTF-8编码或者7-bit ASCII编码的文本。</td>
                    <td align="right">string</td>
                    <td>String</td>
                    <td>str/unicode</td>
                    <td>string</td>
                    <td>String (UTF-8)</td>
                    <td>string</td>
                    <td>string</td>
                </tr>
                <tr>
                    <td>bytes</td>
                    <td align="center">可能包含任意顺序的字节数据。</td>
                    <td align="right">string</td>
                    <td>ByteString</td>
                    <td>str</td>
                    <td>[]byte</td>
                    <td>String (ASCII-8BIT)</td>
                    <td>ByteString</td>
                    <td>string</td>
                </tr>
                </tbody>
            </table>


            <p>你可以在文章<a href="https://developers.google.com/protocol-buffers/docs/encoding?hl=zh-cn" target="_blank"
                        rel="external">Protocol Buffer 编码</a>中，找到更多“序列化消息时各种类型如何编码”的信息。</p>
            <ol>
                <li>在java中，无符号32位和64位整型被表示成他们的整型对应形式，最高位被储存在标志位中。</li>
                <li>对于所有的情况，设定值会执行类型检查以确保此值是有效。</li>
                <li>64位或者无符号32位整型在解码时被表示成为ilong，但是在设置时可以使用int型值设定，在所有的情况下，值必须符合其设置其类型的要求。</li>
                <li>python中string被表示成在解码时表示成unicode。但是一个ASCIIstring可以被表示成str类型。</li>
                <li>Integer在64位的机器上使用，string在32位机器上使用</li>
            </ol>
            <h2 id="默认值">默认值</h2>
            <p>当一个消息被解析的时候，如果被编码的信息不包含一个特定的singular元素，被解析的对象锁对应的域被设置位一个默认值，对于不同类型指定如下：</p>
            <ul>
                <li>对于string，默认是一个空string</li>
                <li>对于bytes，默认是一个空的bytes</li>
                <li>对于bool，默认是false</li>
                <li>对于数值类型，默认是0</li>
                <li>对于枚举，默认是第一个定义的枚举值，必须为0;</li>
                <li>对于消息类型（message），域没有被设置，确切的消息是根据语言确定的，详见generated code guide</li>
            </ul>
            <p>对于可重复域的默认值是空（通常情况下是对应语言中空列表）。</p>
            <p><em>注：对于标量消息域，一旦消息被解析，就无法判断域释放被设置为默认值（例如，例如boolean值是否被设置为false）还是根本没有被设置。你应该在定义你的消息类型时非常注意。例如，比如你不应该定义boolean的默认值false作为任何行为的触发方式。也应该注意如果一个标量消息域被设置为标志位，这个值不应该被序列化传输。</em>
            </p>
            <p>查看<a href="https://developers.google.com/protocol-buffers/docs/reference/overview?hl=zh-cn" target="_blank"
                    rel="external">generated code guide</a>选择你的语言的默认值的工作细节。</p>
            <h2 id="枚举">枚举</h2>
            <p>当需要定义一个消息类型的时候，可能想为一个字段指定某“预定义值序列”中的一个值。例如，假设要为每一个SearchRequest消息添加一个
                corpus字段，而corpus的值可能是UNIVERSAL，WEB，IMAGES，LOCAL，NEWS，PRODUCTS或VIDEO中的一个。
                其实可以很容易地实现这一点：通过向消息定义中添加一个枚举（enum）并且为每个可能的值定义一个常量就可以了。</p>
            <p>在下面的例子中，在消息格式中添加了一个叫做Corpus的枚举类型——它含有所有可能的值 ——以及一个类型为Corpus的字段：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div><div class="line">9</div><div
                            class="line">10</div><div class="line">11</div><div class="line">12</div><div class="line">13</div><div
                            class="line">14</div><div class="line">15</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchRequest</span> </span>{</div><div
                            class="line">  <span class="built_in">string</span> query = <span
                            class="number">1</span>;</div><div class="line">  <span class="built_in">int32</span> page_number = <span
                            class="number">2</span>;</div><div class="line">  <span class="built_in">int32</span> result_per_page = <span
                            class="number">3</span>;</div><div class="line">  <span class="class"><span class="keyword">enum</span> <span
                            class="title">Corpus</span> </span>{</div><div class="line"><span class="constant">    UNIVERSAL</span> = <span
                            class="number">0</span>;</div><div class="line"><span
                            class="constant">    WEB</span> = <span class="number">1</span>;</div><div
                            class="line"><span class="constant">    IMAGES</span> = <span class="number">2</span>;</div><div
                            class="line"><span class="constant">    LOCAL</span> = <span class="number">3</span>;</div><div
                            class="line"><span class="constant">    NEWS</span> = <span class="number">4</span>;</div><div
                            class="line"><span class="constant">    PRODUCTS</span> = <span
                            class="number">5</span>;</div><div class="line"><span
                            class="constant">    VIDEO</span> = <span class="number">6</span>;</div><div class="line">  }</div><div
                            class="line"><span class="constant">  Corpus corpus</span> = <span class="number">4</span>;</div><div
                            class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>如你所见，Corpus枚举的第一个常量映射为0：每个枚举类型必须将其第一个类型映射为0，这是因为：</p>
            <ul>
                <li>必须有有一个0值，我们可以用这个0值作为默认值。</li>
                <li>这个零值必须为第一个元素，为了兼容proto2语义，枚举类的第一个值总是默认值。</li>
            </ul>
            <p>你可以通过将不同的枚举常量指定位相同的值。如果这样做你需要将allow_alias设定位true，否则编译器会在别名的地方产生一个错误信息。</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div><div class="line">9</div><div
                            class="line">10</div><div class="line">11</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">enum</span> <span class="title">EnumAllowingAlias</span> </span>{</div><div
                            class="line">  <span class="keyword">option</span> allow_alias = <span
                            class="literal">true</span>;</div><div class="line"><span class="constant">  UNKNOWN</span> = <span
                            class="number">0</span>;</div><div class="line"><span
                            class="constant">  STARTED</span> = <span class="number">1</span>;</div><div
                            class="line"><span class="constant">  RUNNING</span> = <span class="number">1</span>;</div><div
                            class="line">}</div><div class="line"><span class="class"><span class="keyword">enum</span> <span
                            class="title">EnumNotAllowingAlias</span> </span>{</div><div class="line"><span
                            class="constant">  UNKNOWN</span> = <span class="number">0</span>;</div><div
                            class="line"><span class="constant">  STARTED</span> = <span class="number">1</span>;</div><div
                            class="line">  <span class="comment">// RUNNING = 1;  // Uncommenting this line will cause a compile error inside Google and a warning message outside.</span></div><div
                            class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>枚举常量必须在32位整型值的范围内。因为enum值是使用可变编码方式的，对负数不够高效，因此不推荐在enum中使用负数。如上例所示，可以在
                一个消息定义的内部或外部定义枚举——这些枚举可以在.proto文件中的任何消息定义里重用。当然也可以在一个消息中声明一个枚举类型，而在另一个不同
                的消息中使用它——采用MessageType.EnumType的语法格式。</p>
            <p>当对一个使用了枚举的.proto文件运行protocol buffer编译器的时候，生成的代码中将有一个对应的enum（对Java或C++来说），或者一个特殊的EnumDescriptor类（对
                Python来说），它被用来在运行时生成的类中创建一系列的整型值符号常量（symbolic constants）。</p>
            <p>
                在反序列化的过程中，无法识别的枚举值会被保存在消息中，虽然这种表示方式需要依据所使用语言而定。在那些支持开放枚举类型超出指定范围之外的语言中（例如C++和Go），为识别的值会被表示成所支持的整型。在使用封闭枚举类型的语言中（Java），使用枚举中的一个类型来表示未识别的值，并且可以使用所支持整型来访问。在其他情况下，如果解析的消息被序列号，未识别的值将保持原样。</p>
            <p>关于如何在你的应用程序的消息中使用枚举的更多信息，请查看所选择的语言<a
                    href="http://code.google.com/intl/zh-CN/apis/protocolbuffers/docs/reference/overview.html" target="_blank"
                    rel="external">generated code guide</a>。</p>
            <h2 id="使用其他消息类型">使用其他消息类型</h2>
            <p>
                你可以将其他消息类型用作字段类型。例如，假设在每一个SearchResponse消息中包含Result消息，此时可以在相同的.proto文件中定义一个Result消息类型，然后在SearchResponse消息中指定一个Result类型的字段，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div><div class="line">9</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchResponse</span> </span>{</div><div
                            class="line">  <span class="keyword">repeated</span> Result results = <span
                            class="number">1</span>;</div><div class="line">}</div><div class="line"></div><div
                            class="line"><span class="class"><span class="keyword">message</span> <span class="title">Result</span> </span>{</div><div
                            class="line">  <span class="built_in">string</span> url = <span
                            class="number">1</span>;</div><div class="line">  <span class="built_in">string</span> title = <span
                            class="number">2</span>;</div><div class="line">  <span
                            class="keyword">repeated</span> <span class="built_in">string</span> snippets = <span
                            class="number">3</span>;</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h3 id="导入定义">导入定义</h3>
            <p>在上面的例子中，Result消息类型与SearchResponse是定义在同一文件中的。如果想要使用的消息类型已经在其他.proto文件中已经定义过了呢？<br>你可以通过导入（importing）其他.proto文件中的定义来使用它们。要导入其他.proto文件的定义，你需要在你的文件中添加一个导入声明，如：
            </p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div></pre>
                        </td>
                        <td class="code">
                            <pre><div class="line"><span class="keyword">import</span> <span class="string">"myproject/other_protos.proto"</span>;</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h2 id="嵌套类型">嵌套类型</h2>
            <p>你可以在其他消息类型中定义、使用消息类型，在下面的例子中，Result消息就定义在SearchResponse消息内，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SearchResponse</span> </span>{</div><div
                            class="line">  <span class="class"><span class="keyword">message</span> <span class="title">Result</span> </span>{</div><div
                            class="line">    <span class="built_in">string</span> url = <span
                            class="number">1</span>;</div><div class="line">    <span class="built_in">string</span> title = <span
                            class="number">2</span>;</div><div class="line">    <span
                            class="keyword">repeated</span> <span class="built_in">string</span> snippets = <span
                            class="number">3</span>;</div><div class="line">  }</div><div class="line">  <span
                            class="keyword">repeated</span> Result results = <span class="number">1</span>;</div><div
                            class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>如果你想在它的父消息类型的外部重用这个消息类型，你需要以Parent.Type的形式使用它，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">SomeOtherMessage</span> </span>{</div><div
                            class="line"><span class="constant">  SearchResponse.Result result</span> = <span
                            class="number">1</span>;</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>当然，你也可以将消息嵌套任意多层，如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div><div
                            class="line">7</div><div class="line">8</div><div class="line">9</div><div
                            class="line">10</div><div class="line">11</div><div class="line">12</div><div class="line">13</div><div
                            class="line">14</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">Outer</span> </span>{                  <span
                            class="comment">// Level 0</span></div><div class="line">  <span class="class"><span
                            class="keyword">message</span> <span class="title">MiddleAA</span> </span>{  <span
                            class="comment">// Level 1</span></div><div class="line">    <span class="class"><span
                            class="keyword">message</span> <span class="title">Inner</span> </span>{   <span
                            class="comment">// Level 2</span></div><div class="line">      <span
                            class="built_in">int64</span> ival = <span class="number">1</span>;</div><div class="line">      <span
                            class="built_in">bool</span>  booly = <span class="number">2</span>;</div><div class="line">    }</div><div
                            class="line">  }</div><div class="line">  <span class="class"><span
                            class="keyword">message</span> <span class="title">MiddleBB</span> </span>{  <span
                            class="comment">// Level 1</span></div><div class="line">    <span class="class"><span
                            class="keyword">message</span> <span class="title">Inner</span> </span>{   <span
                            class="comment">// Level 2</span></div><div class="line">      <span
                            class="built_in">int32</span> ival = <span class="number">1</span>;</div><div class="line">      <span
                            class="built_in">bool</span>  booly = <span class="number">2</span>;</div><div class="line">    }</div><div
                            class="line">  }</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h2 id="更新一个消息类型">更新一个消息类型</h2>
            <p>如果一个已有的消息格式已无法满足新的需求——如，要在消息中添加一个额外的字段——但是同时旧版本写的代码仍然可用。不用担心！更新消息而不破坏已有代码是非常简单的。在更新时只要记住以下的规则即可。</p>
            <ul>
                <li>不要更改任何已有的字段的数值标识。</li>
                <li>
                    如果你增加新的字段，使用旧格式的字段仍然可以被你新产生的代码所解析。你应该记住这些元素的默认值这样你的新代码就可以以适当的方式和旧代码产生的数据交互。相似的，通过新代码产生的消息也可以被旧代码解析：只不过新的字段会被忽视掉。注意，未被识别的字段会在反序列化的过程中丢弃掉，所以如果消息再被传递给新的代码，新的字段依然是不可用的（这和proto2中的行为是不同的，在proto2中未定义的域依然会随着消息被序列化）
                </li>
                <li>
                    非required的字段可以移除——只要它们的标识号在新的消息类型中不再使用（更好的做法可能是重命名那个字段，例如在字段前添加“OBSOLETE_”前缀，那样的话，使用的.proto文件的用户将来就不会无意中重新使用了那些不该使用的标识号）。
                </li>
                <li>int32, uint32, int64, uint64,和bool是全部兼容的，这意味着可以将这些类型中的一个转换为另外一个，而不会破坏向前、
                    向后的兼容性。如果解析出来的数字与对应的类型不相符，那么结果就像在C++中对它进行了强制类型转换一样（例如，如果把一个64位数字当作int32来 读取，那么它就会被截断为32位的数字）。
                </li>
                <li>sint32和sint64是互相兼容的，但是它们与其他整数类型不兼容。</li>
                <li>string和bytes是兼容的——只要bytes是有效的UTF-8编码。</li>
                <li>嵌套消息与bytes是兼容的——只要bytes包含该消息的一个编码过的版本。</li>
                <li>fixed32与sfixed32是兼容的，fixed64与sfixed64是兼容的。</li>
                <li>
                    枚举类型与int32，uint32，int64和uint64相兼容（注意如果值不相兼容则会被截断），然而在客户端反序列化之后他们可能会有不同的处理方式，例如，未识别的proto3枚举类型会被保留在消息中，但是他的表示方式会依照语言而定。int类型的字段总会保留他们的
                </li>
            </ul>

            <h2 id="Map">Map</h2>
            <p>如果你希望创建一个关联映射，protocol buffer提供了一种快捷的语法：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div></pre>
                        </td>
                        <td class="code">
                            <pre><div class="line">map&lt;key_type, value_type&gt; map_field = N;</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>其中key_type可以是任意Integer或者string类型（所以，除了floating和bytes的任意标量类型都是可以的）value_type可以是任意类型。</p>
            <p>例如，如果你希望创建一个project的映射，每个Projecct使用一个string作为key，你可以像下面这样定义：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line">map&lt;<span class="built_in">string</span>, Project&gt; projects = <span
                            class="number">3</span>;</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <ul>
                <li>Map的字段可以是repeated。</li>
                <li>序列化后的顺序和map迭代器的顺序是不确定的，所以你不要期望以固定顺序处理Map</li>
                <li>当为.proto文件产生生成文本格式的时候，map会按照key 的顺序排序，数值化的key会按照数值排序。</li>
                <li>从序列化中解析或者融合时，如果有重复的key则后一个key不会被使用，当从文本格式中解析map时，如果存在重复的key。</li>
            </ul>
            <p>生成map的API现在对于所有proto3支持的语言都可用了，你可以从<a
                    href="https://developers.google.com/protocol-buffers/docs/reference/overview" target="_blank"
                    rel="external">API指南</a>找到更多信息。</p>
            <h3 id="向后兼容性问题-1">向后兼容性问题</h3>
            <p>map语法序列化后等同于如下内容，因此即使是不支持map语法的protocol buffer实现也是可以处理你的数据的：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div><div class="line">6</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">message</span> <span class="title">MapFieldEntry</span> </span>{</div><div
                            class="line">  key_type key = <span class="number">1</span>;</div><div class="line">  value_type value = <span
                            class="number">2</span>;</div><div class="line">}</div><div class="line"></div><div
                            class="line"><span class="keyword">repeated</span> MapFieldEntry map_field = N;</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h2 id="Package">Package</h2>
            <p>当然可以为.proto文件新增一个可选的package声明符，用来防止不同的消息类型有命名冲突。如：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div><div class="line">2</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="keyword">package</span> foo.bar;</div><div class="line"><span
                            class="class"><span class="keyword">message</span> <span class="title">Open</span> </span>{ ... }</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>在其他的消息格式定义中可以使用包名+消息名的方式来定义域的类型，如：</p>
            <figure class="highlight go">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                    <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div><div
                            class="line">4</div><div class="line">5</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line">message Foo {</div><div class="line">  ...</div><div class="line">  required foo.bar.Open open =<span
                            class="number"> 1</span>;</div><div class="line">  ...</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <p>包的声明符会根据使用语言的不同影响生成的代码。</p>
            <ul>
                <li>对于C++，产生的类会被包装在C++的命名空间中，如上例中的Open会被封装在 foo::bar空间中； -
                    对于Java，包声明符会变为java的一个包，除非在.proto文件中提供了一个明确有java_package；
                </li>
                <li>对于 Python，这个包声明符是被忽略的，因为Python模块是按照其在文件系统中的位置进行组织的。</li>
                <li>对于Go，包可以被用做Go包名称，除非你显式的提供一个option go_package在你的.proto文件中。</li>
                <li>对于Ruby，生成的类可以被包装在内置的Ruby名称空间中，转换成Ruby所需的大小写样式 （首字母大写；如果第一个符号不是一个字母，则使用PB_前缀），例如Open会在Foo::Bar名称空间中。</li>
                <li>对于javaNano包会使用Java包，除非你在你的文件中显式的提供一个option java_package。</li>
                <li>对于C#包可以转换为PascalCase后作为名称空间，除非你在你的文件中显式的提供一个option csharp_namespace，例如，Open会在Foo.Bar名称空间中</li>
            </ul>
            <h3 id="包及名称的解析">包及名称的解析</h3>
            <p>Protocol buffer语言中类型名称的解析与C++是一致的：首先从最内部开始查找，依次向外进行，每个包会被看作是其父类包的内部类。当然对于 （.foo.bar.Baz）这样以“.”开头的意味着是从最外围开始的。</p>
            <p>ProtocolBuffer编译器会解析.proto文件中定义的所有类型名。 对于不同语言的代码生成器会知道如何来指向每个具体的类型，即使它们使用了不同的规则。</p>
            <h2 id="定义服务(Service)">定义服务(Service)</h2>
            <p>如果想要将消息类型用在RPC(远程方法调用)系统中，可以在.proto文件中定义一个RPC服务接口，protocol
                buffer编译器将会根据所选择的不同语言生成服务接口代码及存根。如，想要定义一个RPC服务并具有一个方法，该方法能够接收
                SearchRequest并返回一个SearchResponse，此时可以在.proto文件中进行如下定义：</p>
            <figure class="highlight protobuf">
                <table>
                    <tbody>
                    <tr>
                        <td class="gutter">
                            <pre><div class="line">1</div><div class="line">2</div><div class="line">3</div></pre>
                        </td>
                        <td class="code">
                    <pre><div class="line"><span class="class"><span class="keyword">service</span> <span class="title">SearchService</span> </span>{</div><div
                            class="line">  <span class="function"><span class="keyword">rpc</span> Search (SearchRequest) <span
                            class="keyword">returns</span> (SearchResponse)</span>;</div><div class="line">}</div></pre>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </figure>

            <h2 id="JSON_映射">JSON 映射</h2>
            <p>Proto3 支持JSON的编码规范，使他更容易在不同系统之间共享数据，在下表中逐个描述类型。</p>
            <p>如果JSON编码的数据丢失或者其本身就是null，这个数据会在解析成protocol buffer的时候被表示成默认值。如果一个字段在protocol
                buffer中表示为默认值，体会在转化成JSON的时候编码的时候忽略掉以节省空间。具体实现可以提供在JSON编码中可选的默认值。</p>
            <table>
                <thead>
                <tr>
                    <th>proto3</th>
                    <th>JSON</th>
                    <th>JSON示例</th>
                    <th>注意</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>message</td>
                    <td>object</td>
                    <td>{“fBar”: v, “g”: null, …}</td>
                    <td>产生JSON对象，消息字段名可以被映射成lowerCamelCase形式，并且成为JSON对象键，null被接受并成为对应字段的默认值</td>
                </tr>
                <tr>
                    <td>enum</td>
                    <td>string</td>
                    <td>“FOO_BAR”</td>
                    <td>枚举值的名字在proto文件中被指定</td>
                </tr>
                <tr>
                    <td>map</td>
                    <td>object</td>
                    <td>{“k”: v, …}</td>
                    <td>所有的键都被转换成string</td>
                </tr>
                <tr>
                    <td>repeated V</td>
                    <td>array</td>
                    <td>[v, …]</td>
                    <td>null被视为空列表</td>
                </tr>
                <tr>
                    <td>bool</td>
                    <td>true, false</td>
                    <td>true, false</td>
                    <td></td>
                </tr>
                <tr>
                    <td>string</td>
                    <td>string</td>
                    <td>“Hello World!”</td>
                    <td></td>
                </tr>
                <tr>
                    <td>bytes</td>
                    <td>base64 string</td>
                    <td>“YWJjMTIzIT8kKiYoKSctPUB+”</td>
                    <td></td>
                </tr>
                <tr>
                    <td>int32, fixed32, uint32</td>
                    <td>number</td>
                    <td>1, -10, 0</td>
                    <td>JSON值会是一个十进制数，数值型或者string类型都会接受</td>
                </tr>
                <tr>
                    <td>int64, fixed64, uint64</td>
                    <td>string</td>
                    <td>“1”, “-10”</td>
                    <td>JSON值会是一个十进制数，数值型或者string类型都会接受</td>
                </tr>
                <tr>
                    <td>float, double</td>
                    <td>number</td>
                    <td>1.1, -10.0, 0, “NaN”, “Infinity”</td>
                    <td>JSON值会是一个数字或者一个指定的字符串如”NaN”,”infinity”或者”-Infinity”，数值型或者字符串都是可接受的，指数符号也可以接受</td>
                </tr>
                <tr>
                    <td>Any</td>
                    <td>object</td>
                    <td>{“@type”: “url”, “f”: v, … }</td>
                    <td>如果一个Any保留一个特上述的JSON映射，则它会转换成一个如下形式：<code>{"@type": xxx, "value": yyy}</code>否则，该值会被转换成一个JSON对象，<code>@type</code>字段会被插入所指定的确定的值
                    </td>
                </tr>
                <tr>
                    <td>Timestamp</td>
                    <td>string</td>
                    <td>“1972-01-01T10:00:20.021Z”</td>
                    <td>使用RFC 339，其中生成的输出将始终是Z-归一化啊的，并且使用0，3，6或者9位小数</td>
                </tr>
                <tr>
                    <td>Duration</td>
                    <td>string</td>
                    <td>“1.000340012s”, “1s”</td>
                    <td>生成的输出总是0，3，6或者9位小数，具体依赖于所需要的精度，接受所有可以转换为纳秒级的精度</td>
                </tr>
                <tr>
                    <td>Struct</td>
                    <td>object</td>
                    <td>{ … }</td>
                    <td>任意的JSON对象，见struct.proto</td>
                </tr>
                <tr>
                    <td>Wrapper types</td>
                    <td>various types</td>
                    <td>2, “2”, “foo”, true, “true”, null, 0, …</td>
                    <td>包装器在JSON中的表示方式类似于基本类型，但是允许nulll，并且在转换的过程中保留null</td>
                </tr>
                <tr>
                    <td>FieldMask</td>
                    <td>string</td>
                    <td>“f.fooBar,h”</td>
                    <td>见fieldmask.proto</td>
                </tr>
                <tr>
                    <td>ListValue</td>
                    <td>array</td>
                    <td>[foo, bar, …]</td>
                    <td></td>
                </tr>
                <tr>
                    <td>Value</td>
                    <td>value</td>
                    <td></td>
                    <td>任意JSON值</td>
                </tr>
                <tr>
                    <td>NullValue</td>
                    <td>null</td>
                    <td></td>
                    <td>JSON null</td>
                </tr>
                </tbody>
            </table>
        </div>
        </div>
    </div>
</template>

<style>
    body { padding: 0; margin: 0; }
.markdown-preview-plus-view:not([data-use-github-style]) { padding: 2em; font-size: 1.2em; color: rgb(171, 178, 191); background-color: rgb(40, 44, 52); overflow: auto; }
.markdown-preview-plus-view:not([data-use-github-style]) > :first-child { margin-top: 0px; }
.markdown-preview-plus-view:not([data-use-github-style]) h1, .markdown-preview-plus-view:not([data-use-github-style]) h2, .markdown-preview-plus-view:not([data-use-github-style]) h3, .markdown-preview-plus-view:not([data-use-github-style]) h4, .markdown-preview-plus-view:not([data-use-github-style]) h5, .markdown-preview-plus-view:not([data-use-github-style]) h6 { line-height: 1.2; margin-top: 1.5em; margin-bottom: 0.5em; color: rgb(255, 255, 255); }
.markdown-preview-plus-view:not([data-use-github-style]) h1 { font-size: 2.4em; font-weight: 300; }
.markdown-preview-plus-view:not([data-use-github-style]) h2 { font-size: 1.8em; font-weight: 400; }
.markdown-preview-plus-view:not([data-use-github-style]) h3 { font-size: 1.5em; font-weight: 500; }
.markdown-preview-plus-view:not([data-use-github-style]) h4 { font-size: 1.2em; font-weight: 600; }
.markdown-preview-plus-view:not([data-use-github-style]) h5 { font-size: 1.1em; font-weight: 600; }
.markdown-preview-plus-view:not([data-use-github-style]) h6 { font-size: 1em; font-weight: 600; }
.markdown-preview-plus-view:not([data-use-github-style]) strong { color: rgb(255, 255, 255); }
.markdown-preview-plus-view:not([data-use-github-style]) del { color: rgb(124, 135, 156); }
.markdown-preview-plus-view:not([data-use-github-style]) a, .markdown-preview-plus-view:not([data-use-github-style]) a code { color: rgb(82, 139, 255); }
.markdown-preview-plus-view:not([data-use-github-style]) img { max-width: 100%; }
.markdown-preview-plus-view:not([data-use-github-style]) > p { margin-top: 0px; margin-bottom: 1.5em; }
.markdown-preview-plus-view:not([data-use-github-style]) > ul, .markdown-preview-plus-view:not([data-use-github-style]) > ol { margin-bottom: 1.5em; }
.markdown-preview-plus-view:not([data-use-github-style]) blockquote { margin: 1.5em 0px; font-size: inherit; color: rgb(124, 135, 156); border-color: rgb(75, 83, 98); border-width: 4px; }
.markdown-preview-plus-view:not([data-use-github-style]) hr { margin: 3em 0px; border-top: 2px dashed rgb(75, 83, 98); background: none; }
.markdown-preview-plus-view:not([data-use-github-style]) table { margin: 1.5em 0px; }
.markdown-preview-plus-view:not([data-use-github-style]) th { color: rgb(255, 255, 255); }
.markdown-preview-plus-view:not([data-use-github-style]) th, .markdown-preview-plus-view:not([data-use-github-style]) td { padding: 0.66em 1em; border: 1px solid rgb(75, 83, 98); }
.markdown-preview-plus-view:not([data-use-github-style]) pre, .markdown-preview-plus-view:not([data-use-github-style]) code { color: rgb(255, 255, 255); background-color: rgb(58, 63, 75); }
.markdown-preview-plus-view:not([data-use-github-style]) pre, .markdown-preview-plus-view:not([data-use-github-style]) pre.editor-colors { margin: 1.5em 0px; padding: 1em; font-size: 0.92em; border-radius: 3px; background-color: rgb(49, 54, 63); }
.markdown-preview-plus-view:not([data-use-github-style]) kbd { color: rgb(255, 255, 255); border-width: 1px 1px 2px; border-style: solid; border-color: rgb(75, 83, 98) rgb(75, 83, 98) rgb(62, 68, 81); border-image: initial; background-color: rgb(58, 63, 75); }
.markdown-preview-plus-view:not([data-use-github-style]) ul.contains-task-list li.task-list-item { position: relative; list-style-type: none; }
.markdown-preview-plus-view:not([data-use-github-style]) ul.contains-task-list li.task-list-item input.task-list-item-checkbox { position: absolute; transform: translateX(-100%); width: 30px; }
.markdown-preview-plus-view[data-use-github-style] { font-family: "Helvetica Neue", Helvetica, "Segoe UI", Arial, freesans, sans-serif; line-height: 1.6; word-wrap: break-word; padding: 0 30px; font-size: 16px; color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); overflow: auto; }
.markdown-preview-plus-view[data-use-github-style] > :first-child { margin-top: 0px !important; }
.markdown-preview-plus-view[data-use-github-style] > :last-child { margin-bottom: 0px !important; }
.markdown-preview-plus-view[data-use-github-style] a:not([href]) { color: inherit; text-decoration: none; }
.markdown-preview-plus-view[data-use-github-style] .absent { color: rgb(204, 0, 0); }
.markdown-preview-plus-view[data-use-github-style] .anchor { position: absolute; top: 0px; left: 0px; display: block; padding-right: 6px; padding-left: 30px; margin-left: -30px; }
.markdown-preview-plus-view[data-use-github-style] .anchor:focus { outline: none; }
.markdown-preview-plus-view[data-use-github-style] h1, .markdown-preview-plus-view[data-use-github-style] h2, .markdown-preview-plus-view[data-use-github-style] h3, .markdown-preview-plus-view[data-use-github-style] h4, .markdown-preview-plus-view[data-use-github-style] h5, .markdown-preview-plus-view[data-use-github-style] h6 { position: relative; margin-top: 1em; margin-bottom: 16px; font-weight: bold; line-height: 1.4; }
.markdown-preview-plus-view[data-use-github-style] h1 .octicon-link, .markdown-preview-plus-view[data-use-github-style] h2 .octicon-link, .markdown-preview-plus-view[data-use-github-style] h3 .octicon-link, .markdown-preview-plus-view[data-use-github-style] h4 .octicon-link, .markdown-preview-plus-view[data-use-github-style] h5 .octicon-link, .markdown-preview-plus-view[data-use-github-style] h6 .octicon-link { display: none; color: rgb(0, 0, 0); vertical-align: middle; }
.markdown-preview-plus-view[data-use-github-style] h1:hover .anchor, .markdown-preview-plus-view[data-use-github-style] h2:hover .anchor, .markdown-preview-plus-view[data-use-github-style] h3:hover .anchor, .markdown-preview-plus-view[data-use-github-style] h4:hover .anchor, .markdown-preview-plus-view[data-use-github-style] h5:hover .anchor, .markdown-preview-plus-view[data-use-github-style] h6:hover .anchor { padding-left: 8px; margin-left: -30px; text-decoration: none; }
.markdown-preview-plus-view[data-use-github-style] h1:hover .anchor .octicon-link, .markdown-preview-plus-view[data-use-github-style] h2:hover .anchor .octicon-link, .markdown-preview-plus-view[data-use-github-style] h3:hover .anchor .octicon-link, .markdown-preview-plus-view[data-use-github-style] h4:hover .anchor .octicon-link, .markdown-preview-plus-view[data-use-github-style] h5:hover .anchor .octicon-link, .markdown-preview-plus-view[data-use-github-style] h6:hover .anchor .octicon-link { display: inline-block; }
.markdown-preview-plus-view[data-use-github-style] h1 tt, .markdown-preview-plus-view[data-use-github-style] h2 tt, .markdown-preview-plus-view[data-use-github-style] h3 tt, .markdown-preview-plus-view[data-use-github-style] h4 tt, .markdown-preview-plus-view[data-use-github-style] h5 tt, .markdown-preview-plus-view[data-use-github-style] h6 tt, .markdown-preview-plus-view[data-use-github-style] h1 code, .markdown-preview-plus-view[data-use-github-style] h2 code, .markdown-preview-plus-view[data-use-github-style] h3 code, .markdown-preview-plus-view[data-use-github-style] h4 code, .markdown-preview-plus-view[data-use-github-style] h5 code, .markdown-preview-plus-view[data-use-github-style] h6 code { font-size: inherit; }
.markdown-preview-plus-view[data-use-github-style] h1 { padding-bottom: 0.3em; font-size: 2.25em; line-height: 1.2; border-bottom: 1px solid rgb(238, 238, 238); }
.markdown-preview-plus-view[data-use-github-style] h1 .anchor { line-height: 1; }
.markdown-preview-plus-view[data-use-github-style] h2 { padding-bottom: 0.3em; font-size: 1.75em; line-height: 1.225; border-bottom: 1px solid rgb(238, 238, 238); }
.markdown-preview-plus-view[data-use-github-style] h2 .anchor { line-height: 1; }
.markdown-preview-plus-view[data-use-github-style] h3 { font-size: 1.5em; line-height: 1.43; }
.markdown-preview-plus-view[data-use-github-style] h3 .anchor { line-height: 1.2; }
.markdown-preview-plus-view[data-use-github-style] h4 { font-size: 1.25em; }
.markdown-preview-plus-view[data-use-github-style] h4 .anchor { line-height: 1.2; }
.markdown-preview-plus-view[data-use-github-style] h5 { font-size: 1em; }
.markdown-preview-plus-view[data-use-github-style] h5 .anchor { line-height: 1.1; }
.markdown-preview-plus-view[data-use-github-style] h6 { font-size: 1em; color: rgb(119, 119, 119); }
.markdown-preview-plus-view[data-use-github-style] h6 .anchor { line-height: 1.1; }
.markdown-preview-plus-view[data-use-github-style] p, .markdown-preview-plus-view[data-use-github-style] blockquote, .markdown-preview-plus-view[data-use-github-style] ul, .markdown-preview-plus-view[data-use-github-style] ol, .markdown-preview-plus-view[data-use-github-style] dl, .markdown-preview-plus-view[data-use-github-style] table, .markdown-preview-plus-view[data-use-github-style] pre { margin-top: 0px; margin-bottom: 16px; }
.markdown-preview-plus-view[data-use-github-style] hr { height: 4px; padding: 0px; margin: 16px 0px; background-color: rgb(231, 231, 231); border: 0px none; }
.markdown-preview-plus-view[data-use-github-style] ul, .markdown-preview-plus-view[data-use-github-style] ol { padding-left: 2em; }
.markdown-preview-plus-view[data-use-github-style] ul.no-list, .markdown-preview-plus-view[data-use-github-style] ol.no-list { padding: 0px; list-style-type: none; }
.markdown-preview-plus-view[data-use-github-style] ul ul, .markdown-preview-plus-view[data-use-github-style] ul ol, .markdown-preview-plus-view[data-use-github-style] ol ol, .markdown-preview-plus-view[data-use-github-style] ol ul { margin-top: 0px; margin-bottom: 0px; }
.markdown-preview-plus-view[data-use-github-style] li > p { margin-top: 16px; }
.markdown-preview-plus-view[data-use-github-style] dl { padding: 0px; }
.markdown-preview-plus-view[data-use-github-style] dl dt { padding: 0px; margin-top: 16px; font-size: 1em; font-style: italic; font-weight: bold; }
.markdown-preview-plus-view[data-use-github-style] dl dd { padding: 0px 16px; margin-bottom: 16px; }
.markdown-preview-plus-view[data-use-github-style] blockquote { padding: 0px 15px; color: rgb(119, 119, 119); border-left: 4px solid rgb(221, 221, 221); }
.markdown-preview-plus-view[data-use-github-style] blockquote > :first-child { margin-top: 0px; }
.markdown-preview-plus-view[data-use-github-style] blockquote > :last-child { margin-bottom: 0px; }
.markdown-preview-plus-view[data-use-github-style] table { display: block; width: 100%; overflow: auto; word-break: keep-all; }
.markdown-preview-plus-view[data-use-github-style] table th { font-weight: bold; }
.markdown-preview-plus-view[data-use-github-style] table th, .markdown-preview-plus-view[data-use-github-style] table td { padding: 6px 13px; border: 1px solid rgb(221, 221, 221); }
.markdown-preview-plus-view[data-use-github-style] table tr { background-color: rgb(255, 255, 255); border-top: 1px solid rgb(204, 204, 204); }
.markdown-preview-plus-view[data-use-github-style] table tr:nth-child(2n) { background-color: rgb(248, 248, 248); }
.markdown-preview-plus-view[data-use-github-style] img { max-width: 100%; box-sizing: border-box; }
.markdown-preview-plus-view[data-use-github-style] .emoji { max-width: none; }
.markdown-preview-plus-view[data-use-github-style] span.frame { display: block; overflow: hidden; }
.markdown-preview-plus-view[data-use-github-style] span.frame > span { display: block; float: left; width: auto; padding: 7px; margin: 13px 0px 0px; overflow: hidden; border: 1px solid rgb(221, 221, 221); }
.markdown-preview-plus-view[data-use-github-style] span.frame span img { display: block; float: left; }
.markdown-preview-plus-view[data-use-github-style] span.frame span span { display: block; padding: 5px 0px 0px; clear: both; color: rgb(51, 51, 51); }
.markdown-preview-plus-view[data-use-github-style] span.align-center { display: block; overflow: hidden; clear: both; }
.markdown-preview-plus-view[data-use-github-style] span.align-center > span { display: block; margin: 13px auto 0px; overflow: hidden; text-align: center; }
.markdown-preview-plus-view[data-use-github-style] span.align-center span img { margin: 0px auto; text-align: center; }
.markdown-preview-plus-view[data-use-github-style] span.align-right { display: block; overflow: hidden; clear: both; }
.markdown-preview-plus-view[data-use-github-style] span.align-right > span { display: block; margin: 13px 0px 0px; overflow: hidden; text-align: right; }
.markdown-preview-plus-view[data-use-github-style] span.align-right span img { margin: 0px; text-align: right; }
.markdown-preview-plus-view[data-use-github-style] span.float-left { display: block; float: left; margin-right: 13px; overflow: hidden; }
.markdown-preview-plus-view[data-use-github-style] span.float-left span { margin: 13px 0px 0px; }
.markdown-preview-plus-view[data-use-github-style] span.float-right { display: block; float: right; margin-left: 13px; overflow: hidden; }
.markdown-preview-plus-view[data-use-github-style] span.float-right > span { display: block; margin: 13px auto 0px; overflow: hidden; text-align: right; }
.markdown-preview-plus-view[data-use-github-style] code, .markdown-preview-plus-view[data-use-github-style] tt { padding: 0.2em 0px; margin: 0px; font-size: 85%; background-color: rgba(0, 0, 0, 0.04); border-radius: 3px; }
.markdown-preview-plus-view[data-use-github-style] code::before, .markdown-preview-plus-view[data-use-github-style] tt::before, .markdown-preview-plus-view[data-use-github-style] code::after, .markdown-preview-plus-view[data-use-github-style] tt::after { letter-spacing: -0.2em; content: " "; }
.markdown-preview-plus-view[data-use-github-style] code br, .markdown-preview-plus-view[data-use-github-style] tt br { display: none; }
.markdown-preview-plus-view[data-use-github-style] del code { text-decoration: inherit; }
.markdown-preview-plus-view[data-use-github-style] pre > code { padding: 0px; margin: 0px; font-size: 100%; word-break: normal; white-space: pre; background: transparent; border: 0px; }
.markdown-preview-plus-view[data-use-github-style] .highlight { margin-bottom: 16px; }
.markdown-preview-plus-view[data-use-github-style] .highlight pre, .markdown-preview-plus-view[data-use-github-style] pre { padding: 16px; overflow: auto; font-size: 85%; line-height: 1.45; background-color: rgb(247, 247, 247); border-radius: 3px; }
.markdown-preview-plus-view[data-use-github-style] .highlight pre { margin-bottom: 0px; word-break: normal; }
.markdown-preview-plus-view[data-use-github-style] pre { word-wrap: normal; }
.markdown-preview-plus-view[data-use-github-style] pre code, .markdown-preview-plus-view[data-use-github-style] pre tt { display: inline; max-width: initial; padding: 0px; margin: 0px; overflow: initial; line-height: inherit; word-wrap: normal; background-color: transparent; border: 0px; }
.markdown-preview-plus-view[data-use-github-style] pre code::before, .markdown-preview-plus-view[data-use-github-style] pre tt::before, .markdown-preview-plus-view[data-use-github-style] pre code::after, .markdown-preview-plus-view[data-use-github-style] pre tt::after { content: normal; }
.markdown-preview-plus-view[data-use-github-style] kbd { display: inline-block; padding: 3px 5px; font-size: 11px; line-height: 10px; color: rgb(85, 85, 85); vertical-align: middle; background-color: rgb(252, 252, 252); border-width: 1px; border-style: solid; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(187, 187, 187); border-image: initial; border-radius: 3px; box-shadow: rgb(187, 187, 187) 0px -1px 0px inset; }
.markdown-preview-plus-view[data-use-github-style] a { color: rgb(51, 122, 183); }
.markdown-preview-plus-view[data-use-github-style] pre, .markdown-preview-plus-view[data-use-github-style] code { color: inherit; }
.markdown-preview-plus-view[data-use-github-style] pre, .markdown-preview-plus-view[data-use-github-style] pre.editor-colors { padding: 0.8em 1em; margin-bottom: 1em; font-size: 0.85em; border-radius: 4px; overflow: auto; }
.markdown-preview-plus-view[data-use-github-style] ul.contains-task-list li.task-list-item { position: relative; list-style-type: none; }
.markdown-preview-plus-view[data-use-github-style] ul.contains-task-list li.task-list-item input.task-list-item-checkbox { position: absolute; transform: translateX(-100%); width: 30px; }
.markdown-preview-plus-view { height: 100%; display: block; }
.markdown-preview-plus-view .emoji { max-width: 1em !important; }
.scrollbars-visible-always .markdown-preview-plus-view pre.editor-colors .vertical-scrollbar, .scrollbars-visible-always .markdown-preview-plus-view pre.editor-colors .horizontal-scrollbar { visibility: hidden; }
.scrollbars-visible-always .markdown-preview-plus-view pre.editor-colors:hover .vertical-scrollbar, .scrollbars-visible-always .markdown-preview-plus-view pre.editor-colors:hover .horizontal-scrollbar { visibility: visible; }
.markdown-preview-plus-view del { text-decoration: none; position: relative; }
.markdown-preview-plus-view del::after { border-bottom: 1px solid black; content: ""; left: 0px; position: absolute; right: 0px; top: 50%; }
.markdown-preview-plus-view .flash { animation: flash 1s ease-out 1; outline: rgba(255, 0, 0, 0) solid 1px; }
.markdown-preview-plus-view .flash:not(li) { display: block; }
.bracket-matcher .region {
    border-bottom: 1px dotted lime;
    position: absolute;
}
.line-number.bracket-matcher.bracket-matcher {
    color: #abb2bf;
    background-color: #3a3f4b;
}

.spell-check-misspelling .region {
    border-bottom: 2px dotted rgba(255, 51, 51, 0.75);
}
.spell-check-corrections {
    width: 25em !important;
}

pre.editor-colors {
    background-color: #282c34;
    color: #abb2bf;
}
pre.editor-colors .line.cursor-line {
    background-color: rgba(153, 187, 255, 0.04);
}
pre.editor-colors .invisible {
    color: #abb2bf;
}
pre.editor-colors .cursor {
    border-left: 2px solid #528bff;
}
pre.editor-colors .selection .region {
    background-color: #3e4451;
}
pre.editor-colors .bracket-matcher .region {
    border-bottom: 1px solid #528bff;
    box-sizing: border-box;
}
pre.editor-colors .invisible-character {
    color: rgba(171, 178, 191, 0.15);
}
pre.editor-colors .indent-guide {
    color: rgba(171, 178, 191, 0.15);
}
pre.editor-colors .wrap-guide {
    background-color: rgba(171, 178, 191, 0.15);
}
pre.editor-colors .find-result .region.region.region,
pre.editor-colors .current-result .region.region.region {
    border-radius: 2px;
    background-color: rgba(82, 139, 255, 0.24);
    transition: border-color 0.4s;
}
pre.editor-colors .find-result .region.region.region {
    border: 2px solid transparent;
}
pre.editor-colors .current-result .region.region.region {
    border: 2px solid #528bff;
    transition-duration: .1s;
}
pre.editor-colors .gutter .line-number {
    color: #636d83;
    -webkit-font-smoothing: antialiased;
}
pre.editor-colors .gutter .line-number.cursor-line {
    color: #abb2bf;
    background-color: #3a3f4b;
}
pre.editor-colors .gutter .line-number.cursor-line-no-selection {
    background-color: transparent;
}
pre.editor-colors .gutter .line-number .icon-right {
    color: #abb2bf;
}
pre.editor-colors .gutter:not(.git-diff-icon) .line-number.git-line-removed.git-line-removed::before {
    bottom: -3px;
}
pre.editor-colors .gutter:not(.git-diff-icon) .line-number.git-line-removed::after {
    content: "";
    position: absolute;
    left: 0px;
    bottom: 0px;
    width: 25px;
    border-bottom: 1px dotted rgba(224, 82, 82, 0.5);
    pointer-events: none;
}
pre.editor-colors .gutter .line-number.folded,
pre.editor-colors .gutter .line-number:after,
pre.editor-colors .fold-marker:after {
    color: #abb2bf;
}
.syntax--comment {
    color: #5c6370;
    font-style: italic;
}
.syntax--comment .syntax--markup.syntax--link {
    color: #5c6370;
}
.syntax--entity.syntax--name.syntax--type {
    color: #e5c07b;
}
.syntax--entity.syntax--other.syntax--inherited-class {
    color: #98c379;
}
.syntax--keyword {
    color: #c678dd;
}
.syntax--keyword.syntax--control {
    color: #c678dd;
}
.syntax--keyword.syntax--operator {
    color: #abb2bf;
}
.syntax--keyword.syntax--other.syntax--special-method {
    color: #61afef;
}
.syntax--keyword.syntax--other.syntax--unit {
    color: #d19a66;
}
.syntax--storage {
    color: #c678dd;
}
.syntax--storage.syntax--type.syntax--annotation,
.syntax--storage.syntax--type.syntax--primitive {
    color: #c678dd;
}
.syntax--storage.syntax--modifier.syntax--package,
.syntax--storage.syntax--modifier.syntax--import {
    color: #abb2bf;
}
.syntax--constant {
    color: #d19a66;
}
.syntax--constant.syntax--variable {
    color: #d19a66;
}
.syntax--constant.syntax--character.syntax--escape {
    color: #56b6c2;
}
.syntax--constant.syntax--numeric {
    color: #d19a66;
}
.syntax--constant.syntax--other.syntax--color {
    color: #56b6c2;
}
.syntax--constant.syntax--other.syntax--symbol {
    color: #56b6c2;
}
.syntax--variable {
    color: #e06c75;
}
.syntax--variable.syntax--interpolation {
    color: #be5046;
}
.syntax--variable.syntax--parameter {
    color: #abb2bf;
}
.syntax--string {
    color: #98c379;
}
.syntax--string.syntax--regexp {
    color: #56b6c2;
}
.syntax--string.syntax--regexp .syntax--source.syntax--ruby.syntax--embedded {
    color: #e5c07b;
}
.syntax--string.syntax--other.syntax--link {
    color: #e06c75;
}
.syntax--punctuation.syntax--definition.syntax--comment {
    color: #5c6370;
}
.syntax--punctuation.syntax--definition.syntax--method-parameters,
.syntax--punctuation.syntax--definition.syntax--function-parameters,
.syntax--punctuation.syntax--definition.syntax--parameters,
.syntax--punctuation.syntax--definition.syntax--separator,
.syntax--punctuation.syntax--definition.syntax--seperator,
.syntax--punctuation.syntax--definition.syntax--array {
    color: #abb2bf;
}
.syntax--punctuation.syntax--definition.syntax--heading,
.syntax--punctuation.syntax--definition.syntax--identity {
    color: #61afef;
}
.syntax--punctuation.syntax--definition.syntax--bold {
    color: #e5c07b;
    font-weight: bold;
}
.syntax--punctuation.syntax--definition.syntax--italic {
    color: #c678dd;
    font-style: italic;
}
.syntax--punctuation.syntax--section.syntax--embedded {
    color: #be5046;
}
.syntax--punctuation.syntax--section.syntax--method,
.syntax--punctuation.syntax--section.syntax--class,
.syntax--punctuation.syntax--section.syntax--inner-class {
    color: #abb2bf;
}
.syntax--support.syntax--class {
    color: #e5c07b;
}
.syntax--support.syntax--type {
    color: #56b6c2;
}
.syntax--support.syntax--function {
    color: #56b6c2;
}
.syntax--support.syntax--function.syntax--any-method {
    color: #61afef;
}
.syntax--entity.syntax--name.syntax--function {
    color: #61afef;
}
.syntax--entity.syntax--name.syntax--class,
.syntax--entity.syntax--name.syntax--type.syntax--class {
    color: #e5c07b;
}
.syntax--entity.syntax--name.syntax--section {
    color: #61afef;
}
.syntax--entity.syntax--name.syntax--tag {
    color: #e06c75;
}
.syntax--entity.syntax--other.syntax--attribute-name {
    color: #d19a66;
}
.syntax--entity.syntax--other.syntax--attribute-name.syntax--id {
    color: #61afef;
}
.syntax--meta.syntax--class {
    color: #e5c07b;
}
.syntax--meta.syntax--class.syntax--body {
    color: #abb2bf;
}
.syntax--meta.syntax--method-call,
.syntax--meta.syntax--method {
    color: #abb2bf;
}
.syntax--meta.syntax--definition.syntax--variable {
    color: #e06c75;
}
.syntax--meta.syntax--link {
    color: #d19a66;
}
.syntax--meta.syntax--require {
    color: #61afef;
}
.syntax--meta.syntax--selector {
    color: #c678dd;
}
.syntax--meta.syntax--separator {
    color: #abb2bf;
}
.syntax--meta.syntax--tag {
    color: #abb2bf;
}
.syntax--underline {
    text-decoration: underline;
}
.syntax--none {
    color: #abb2bf;
}
.syntax--invalid.syntax--deprecated {
    color: #523d14 !important;
    background-color: #e0c285 !important;
}
.syntax--invalid.syntax--illegal {
    color: white !important;
    background-color: #e05252 !important;
}
.syntax--markup.syntax--bold {
    color: #d19a66;
    font-weight: bold;
}
.syntax--markup.syntax--changed {
    color: #c678dd;
}
.syntax--markup.syntax--deleted {
    color: #e06c75;
}
.syntax--markup.syntax--italic {
    color: #c678dd;
    font-style: italic;
}
.syntax--markup.syntax--heading {
    color: #e06c75;
}
.syntax--markup.syntax--heading .syntax--punctuation.syntax--definition.syntax--heading {
    color: #61afef;
}
.syntax--markup.syntax--link {
    color: #56b6c2;
}
.syntax--markup.syntax--inserted {
    color: #98c379;
}
.syntax--markup.syntax--quote {
    color: #d19a66;
}
.syntax--markup.syntax--raw {
    color: #98c379;
}
.syntax--source.syntax--c .syntax--keyword.syntax--operator {
    color: #c678dd;
}
.syntax--source.syntax--cpp .syntax--keyword.syntax--operator {
    color: #c678dd;
}
.syntax--source.syntax--cs .syntax--keyword.syntax--operator {
    color: #c678dd;
}
.syntax--source.syntax--css .syntax--property-name,
.syntax--source.syntax--css .syntax--property-value {
    color: #828997;
}
.syntax--source.syntax--css .syntax--property-name.syntax--support,
.syntax--source.syntax--css .syntax--property-value.syntax--support {
    color: #abb2bf;
}
.syntax--source.syntax--elixir .syntax--source.syntax--embedded.syntax--source {
    color: #abb2bf;
}
.syntax--source.syntax--elixir .syntax--constant.syntax--language,
.syntax--source.syntax--elixir .syntax--constant.syntax--numeric,
.syntax--source.syntax--elixir .syntax--constant.syntax--definition {
    color: #61afef;
}
.syntax--source.syntax--elixir .syntax--variable.syntax--definition,
.syntax--source.syntax--elixir .syntax--variable.syntax--anonymous {
    color: #c678dd;
}
.syntax--source.syntax--elixir .syntax--parameter.syntax--variable.syntax--function {
    color: #d19a66;
    font-style: italic;
}
.syntax--source.syntax--elixir .syntax--quoted {
    color: #98c379;
}
.syntax--source.syntax--elixir .syntax--keyword.syntax--special-method,
.syntax--source.syntax--elixir .syntax--embedded.syntax--section,
.syntax--source.syntax--elixir .syntax--embedded.syntax--source.syntax--empty {
    color: #e06c75;
}
.syntax--source.syntax--elixir .syntax--readwrite.syntax--module .syntax--punctuation {
    color: #e06c75;
}
.syntax--source.syntax--elixir .syntax--regexp.syntax--section,
.syntax--source.syntax--elixir .syntax--regexp.syntax--string {
    color: #be5046;
}
.syntax--source.syntax--elixir .syntax--separator,
.syntax--source.syntax--elixir .syntax--keyword.syntax--operator {
    color: #d19a66;
}
.syntax--source.syntax--elixir .syntax--variable.syntax--constant {
    color: #e5c07b;
}
.syntax--source.syntax--elixir .syntax--array,
.syntax--source.syntax--elixir .syntax--scope,
.syntax--source.syntax--elixir .syntax--section {
    color: #828997;
}
.syntax--source.syntax--gfm .syntax--markup {
    -webkit-font-smoothing: auto;
}
.syntax--source.syntax--gfm .syntax--link .syntax--entity {
    color: #61afef;
}
.syntax--source.syntax--go .syntax--storage.syntax--type.syntax--string {
    color: #c678dd;
}
.syntax--source.syntax--ini .syntax--keyword.syntax--other.syntax--definition.syntax--ini {
    color: #e06c75;
}
.syntax--source.syntax--java .syntax--storage.syntax--modifier.syntax--import {
    color: #e5c07b;
}
.syntax--source.syntax--java .syntax--storage.syntax--type {
    color: #e5c07b;
}
.syntax--source.syntax--java .syntax--keyword.syntax--operator.syntax--instanceof {
    color: #c678dd;
}
.syntax--source.syntax--java-properties .syntax--meta.syntax--key-pair {
    color: #e06c75;
}
.syntax--source.syntax--java-properties .syntax--meta.syntax--key-pair > .syntax--punctuation {
    color: #abb2bf;
}
.syntax--source.syntax--js .syntax--keyword.syntax--operator {
    color: #56b6c2;
}
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--delete,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--in,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--of,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--instanceof,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--new,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--typeof,
.syntax--source.syntax--js .syntax--keyword.syntax--operator.syntax--void {
    color: #c678dd;
}
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--dictionary.syntax--json > .syntax--string.syntax--quoted.syntax--json {
    color: #e06c75;
}
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--dictionary.syntax--json > .syntax--string.syntax--quoted.syntax--json > .syntax--punctuation.syntax--string {
    color: #e06c75;
}
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--dictionary.syntax--json > .syntax--value.syntax--json > .syntax--string.syntax--quoted.syntax--json,
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--array.syntax--json > .syntax--value.syntax--json > .syntax--string.syntax--quoted.syntax--json,
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--dictionary.syntax--json > .syntax--value.syntax--json > .syntax--string.syntax--quoted.syntax--json > .syntax--punctuation,
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--array.syntax--json > .syntax--value.syntax--json > .syntax--string.syntax--quoted.syntax--json > .syntax--punctuation {
    color: #98c379;
}
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--dictionary.syntax--json > .syntax--constant.syntax--language.syntax--json,
.syntax--source.syntax--json .syntax--meta.syntax--structure.syntax--array.syntax--json > .syntax--constant.syntax--language.syntax--json {
    color: #56b6c2;
}
.syntax--source.syntax--ruby .syntax--constant.syntax--other.syntax--symbol > .syntax--punctuation {
    color: inherit;
}
.syntax--source.syntax--php .syntax--class.syntax--bracket {
    color: #abb2bf;
}
.syntax--source.syntax--python .syntax--keyword.syntax--operator.syntax--logical.syntax--python {
    color: #c678dd;
}
.syntax--source.syntax--python .syntax--variable.syntax--parameter {
    color: #d19a66;
}

pre.editor-colors { overflow: auto; }</style>


<!--<script>-->
    <!--export default {-->
        <!--data: function () {-->
            <!--return {-->
                <!--abc: "abc"-->
            <!--}-->

        <!--}-->

    <!--}-->
<!--</script>-->