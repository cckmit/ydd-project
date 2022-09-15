var urls = {
    data: getServerHttp() + '/api/depart/list_tree',
    user_data: getServerHttp() + '/api/usr/user_list'
}
Vue.prototype.$axios = axios;
var templateStr = createVueTemplate(function () {
    /*@preserve
        <div class="content" style="padding:20px;background: #fff;">
            <a-form class="assignmentSetForm" :form="form" @submit="save">
            	<a-row>
			      	<a-col :span="10">
						<a-tree @select="select" :tree-data="treeData" style="height:250px;border:1px solid #d9d9d9;overflow-y: scroll;"/>
		                <a-list size="small" :dataSource="userData" style="height:250px;border:1px solid #d9d9d9;overflow-y: scroll;">
					      <a-list-item slot="renderItem" slot-scope="item, index"><a-checkbox @change="chose(item.OBJECT_ID,item.USR_NAME)">{{item.USR_NAME}}</a-checkbox></a-list-item>
					    </a-list>
					</a-col>
					<a-col :span="4">
						<a-row style="margin-top:220px; margin-left:55px">
							<a-button type="primary" @click="turnIn"><a-icon type="double-right"/></a-button>
						</a-row>
  						<a-row style="margin-top:20px; margin-left:55px">
  							<a-button type="primary" @click="turnOut"><a-icon type="double-left"/></a-button>
						</a-row>
					</a-col>
  					<a-col :span="10">
  						<a-list bordered :dataSource="choseData" style="height:500px;overflow-y: scroll;">
					      <a-list-item slot="renderItem" slot-scope="item, index"><a-checkbox @change="choseD(item.id,item.data)">{{item.data}}</a-checkbox></a-list-item>
					    </a-list>
					</a-col>
			    </a-row>
                <a-form-item :wrapper-col="{ span: 20, offset: 3 }">
                    <a-button type="primary" html-type="submit">
                        确认
                    </a-button>
                </a-form-item>
            </a-form>
        </div>
        */
});

var assignmentSetForm = {
    name: 'assignmentSetForm',
    components: {
    },
    data() {
        return {
            form: this.$form.createForm(this, { name: 'docfileForm' }),
            treeData:[],
            userData:[],
            choseData:[],
            depart:{},// 当前选中的部门
            current:[],// 当前选中的人员
            cData:[],// 当前选中的已选中列表中数据,
            type:getUrlParam('type')
        }
    },
    mounted: function () {
    },
    created: function () {
    	this.fetch();
    },
    methods: {
    	turnIn(){
    		if(this.depart.id==null){
    			this.$message.info("请选择要加入的部门或人员");
				return;
    		}
    		if(this.current.length>0){
    			for(var i=0; i<this.current.length; i++){
    				if(!this.checkChoseData(this.current[i])){
    					this.choseData.push(this.current[i]);
    				}
            	}
    		}else{
    			if(!this.checkChoseData(this.depart)){
					this.choseData.push(this.depart);
				}
    		}
    	},
    	checkChoseData(data){
    		for(var i=0;i<this.choseData.length;i++){
				if(this.choseData[i].id==data.id){
					return true;
				}
			}
    		return false;
    	},
    	turnOut(){
    		for(var i=0;i<this.cData.length;i++){
				this.remove(this.cData[i]);
			}
    	},
    	remove:function(data){
    		for(var i=0;i<this.choseData.length;i++){
				if(this.choseData[i].id==data.id){
					this.choseData.splice(i,1);
				}
			}
    	},
        select(data,info) {
    		this.userData = [];
    		this.current = [];
        	this.depart = {
        		id: data.toString(),
        		data: '[部门]'+info.node.title,
        		type:'org'
        	};
            this.getUsers(data.toString());
        },
        choseD:function(id, name){
        	var isHave = false;
        	for(var i=0; i<this.cData.length; i++){
        		if(this.cData[i].id==id){
        			isHave = true;
        			this.cData.splice(i,1);
        		}
        	}
        	if(!isHave){
        		this.cData.push({
        			id: id,
        			data: '[人员]'+name,
        			type:'user'
        		})
        	}
        },
        chose:function(kid, name){
        	var isHave = false;
        	for(var i=0; i<this.current.length; i++){
        		if(this.current[i].id==kid){
        			isHave = true;
        			this.current.splice(i,1);
        		}
        	}
        	if(!isHave){
        		this.current.push({
        			id: kid,
        			data: '[人员]'+name
        		})
        	}
        },
        fetch:function(){
        	axios.get(urls.data)
	            .then(res => {
	                this.treeData = res.data.result;
	            });
        },
        getUsers(depart_id){
        	axios.get(urls.user_data,{params:{depart_id : depart_id}})
            .then(res => {
                this.userData = res.data.result;
            });
        },
        save: function (e) {
            e.preventDefault();
            var ids='',names='',dataLength = this.choseData.length;
            for(var i=0;i<dataLength;i++){
            	if(i==0){
            		ids = this.choseData[i].id;
            		names = this.choseData[i].data;
            	}else{
            		ids = ids + ','+this.choseData[i].id;
            		names = names + ','+this.choseData[i].data;
            	}
				
			}
            if(dataLength==0){
            	alert('请选择部门或人员')
            	return ;
            }
            if(this.type==1 && dataLength>1){
            	alert('请选择单个部门或人员')
            	return ;
            }
            this.$parent.close(ids,names)
        }
    },
    template: templateStr
}

var assignmentSet = new Vue({
	el: '#app',
	components: {
		'assignmentSetForm': assignmentSetForm,
	},
	data: {
		a:123
	},
	created: function () {
	},
	methods: {
		close(ids,names){
			window.opener.setVal(ids,names);
		}
	}
})

