<style lang="less">
    @import './login.less';
</style>

<template>
    <div class="login" @keydown.enter="handleSubmit" >
        <div class="login-con">
            <Card :bordered="false">
                <div class="login-title" slot="title">
                    <img src="../assets/images/login/logo.png" width="80" height="76" />
                    <p class="sub-title">SpringCloud & Vue2.x zjccloud 方案</p>
                </div>
                <div class="form-con">
                    <Form ref="form" :model="form" :rules="rules">
                        <FormItem prop="userName">
                            <Input size="large" v-model="form.userName" placeholder="请输入用户名" icon="person" />
                        </FormItem>
                        <FormItem prop="password">
                            <Input size="large" type="password" v-model="form.password" placeholder="请输入密码" icon="locked"/>
                        </FormItem>
                        <FormItem prop="picLyanzhengma" >
                        <input type="text" placeholder="请输入验证码"   @blur="checkLpicma" v-model="form.picLyanzhengma">
	                    <input type="text"  style="background-color:green;text-align:center; vertical-align:midde" readonly   id="code" @click="createCode"  v-model="form.checkCode"/> <br>
                        </FormItem>

                        <FormItem>
                            <Button size="large" @click="handleSubmit" class="form-submit" :loading="loading">
                                <span v-if="!loading"><Icon type="checkmark-circled"></Icon> 登入系统</span>
                                <span v-else>正在登陆..</span>
                            </Button>
                        </FormItem>
                    </Form>
                    <p class="login-tip">&copy; 2019 zjccloud </p>
                </div>
            </Card>
        </div>
    </div>
</template>

<script>
var code;
export default {
    data () {
        return {
            form: {
                userName: '',
                password: '',
                picLyanzhengma: '',
                checkCode: ''
            },
            rules: {
                userName: [{required: true, message: '账号不能为空', trigger: 'blur'}],
                password: [{required: true, message: '密码不能为空', trigger: 'blur'}],
                picLyanzhengma: [{required: true, message: '验证码不对', trigger: 'blur'}]
            },
            loading: false
        };
    },
    methods: {

        // 图片验证码

        createCode () {
            code = '';

            var codeLength = 4;// 验证码的长度

            var random = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',

                'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');// 随机数

            for (var i = 0; i < codeLength; i++) { // 循环操作
                var index = Math.floor(Math.random() * 36);// 取得随机数的索引（0~35）

                code += random[index];// 根据索引取得随机数加到code上
            }

            this.form.checkCode = code;// 把code值赋给验证码
        },

        // 失焦验证图和密码

        checkLpicma () {
        //  this.picLyanzhengma.toUpperCase();//取得输入的验证码并转化为大写

            if (this.form.picLyanzhengma == '') {
                false;
            } else if (this.form.picLyanzhengma != this.form.checkCode) { // 若输入的验证码与产生的验证码不一致时
                console.log(this.form.picLyanzhengma);

                console.log(code);

                this.createCode();// 刷新验证码

                this.form.picLyanzhengma = '';

                false;
            } else { // 输入正确时
                return true;
            }
        },

        handleSubmit () {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.checkLpicma() == false) {
                        alert('error');
                        return;
                    }
                    this.loading = true;
                    this.$store.dispatch('Login', this.form).then(() => {
                        this.$router.push('/home');
                        // this.loading = false;
                    }).catch((res) => {
                        console.log(res);
                        this.loading = false;
                    });
                }
            });
        }
    },

    created () {
        this.createCode();
    }

};
</script>

<style>

</style>
