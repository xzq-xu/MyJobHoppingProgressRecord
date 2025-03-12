# RAG技术概览

> https://zhuanlan.zhihu.com/p/678893732


检索增强生成（Retrieval Augmented Generation，简称RAG）

## 基础RAG

我认为的基础RAG是以一系列文本资料作为起点。 当然在这之前还有一些数据获取的步骤，不过这些我认为他们是独立与RAG之外的部分。

简单描述一下就是将一系列文本资料作为数据源（Database） ， 在接收到一个用户提问（query）的时候，在Database中查找相关联的资料文本，然后组合成一个向大模型（LLM） 提问的 prompt，最后拿到LLM的输出（answer）

当然这个从Database中查找相关联资料的过程一般会用到文本嵌入模型（embedding），从Database中根据向量相似度查找

示意流程图
``` mermaid
graph LR
    A(query) --> B([embedding]) --> C[向量化数据] --通过向量相似度算法检索--> D[(Database)]  

    --> E(组装prompt)  --> F([LLM]) --> G(answer)

```

这个Database是我们提前初始化过的，


