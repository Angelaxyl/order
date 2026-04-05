module.exports = {
    plugins: {
        'postcss-pxtorem': {
            rootValue: 75, //设置根元素字体大小，值为移动端页面宽度/10
            propList: ['*'], //可以从px更改到rem的属性
            exclude:['node_modules|components|element-ui|iconfont'], //排除node_modules目录和components下的所有文件
            selectorBlackList: ['vant-','.my-'], // 过滤掉vant-开头的元素选择器、.my-开头的类选择器
        },
    },
};