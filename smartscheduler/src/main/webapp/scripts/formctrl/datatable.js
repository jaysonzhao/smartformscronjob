/**
 * 表单控件-数据表格脚本
 */
//数据表格数据实体
smartforms.formctrl.DataTableEntity = Class.extend({
    /**
     * @constructor
     * 初始化数据表格
     */
    init: function(obj) {
        this.increment = 10;
        this.size = 0;
        this.data = new Array(this.increment);
        if(typeof obj !=="undefined"){
            $.each(obj, $.proxy(function(i,e) {this.add(e);}, this));
        }
    },

    getSize: function() {
        return this.size;
    },

    isEmpty: function() {
        return this.getSize() === 0;
    },

    first: function() {
        if (this.data[0] !== null && typeof this.data[0] !=="undefined") {
           return this.data[0];
        }
        return null;
    },

    last: function() {
        if (this.data[this.getSize() - 1] !== null) {
           return this.data[this.getSize() - 1];
        }
        return null;
    },

    get: function(index) {
        return this.data[index];
    },

    getAllData: function() {
        var retData=[];
        $.each(this.data, function(index, item) {
            if (item!=undefined) {
                retData[index]=item;
            }
        });
        return retData;
    },

    resize:function() {
        var newData = new Array(this.data.length + this.increment);
        for (var i=0; i< this.data.length; i++) {
           newData[i] = this.data[i];
        }
        this.data = newData;
        return this;
     },

    add: function(obj) {
        if(this.getSize() >= this.data.length) {
           this.resize();
        }
        this.data[this.size] = obj;
        this.size++;
        return this;
    },

    insert:function(obj, index) {
        if (this.getSize() >= this.data.length) {
           this.resize();
        }
        for (var i=this.getSize(); i > index; i--) {
           this.data[i] = this.data[i-1];
        }
        this.data[index] = obj;
        this.size++;
        return this;
    },
    
    insertByKey:function(obj, field, value) {
    	var index=this.findIndex(field, value);
    	if (index>=0) {
    		return this.insert(obj, index);
    	} else {
    		return null;
    	}
    },

    remove: function(index) {
        var element = this.data[index];    
        for (var i=index; i<(this.size-1); i++) {
           this.data[i] = this.data[i+1];
        }
        this.data[this.size-1] = null;
        this.size--;
        return element;
    },

    removeAll: function() {
        this.data = new Array(this.increment);
        this.size=0;
    },
    
    removeByKey: function(field, value) {
    	var elm=null;
    	var pos=this.findIndex(field, value);
    	if (pos!=-1) {
    		elm=this.remove(pos);
    	}
    	return elm;
    },
    
    find: function(field, value) {
    	var result=null;
    	for (var i=0; i<this.size; i++) {
    		var element=this.data[i];
    		var cval=element[field];
    		if (typeof cval != "undefined" && cval==value) {
    			result=element;
    			break;
    		}
    	}
    	return result;
    },
    
    findIndex: function(field, value) {
    	var result=-1;
    	for (var i=0; i<this.size; i++) {
    		var element=this.data[i];
    		var cval=element[field];
    		if (typeof cval != "undefined" && cval==value) {
    			result=i;
    			break;
    		}
    	}
    	return result;
    },
    
    /**
     * 对数据源进行排序。
     * @param field  需要排序的字段名称。如果在数据源中找不到该字段，则数据集不会发生重新排序。
     * @param fieldType  排序字段类型，只支持文本(string)和整数(int)两种类型。
     * @param order  排序顺序，asc或者desc，默认asc。
     */
    sortBy: function(field, fieldType, order) {
    	//在数据源中添加一个临时排序字段
    	var tmpDataSource=_.clone(this.data);
    	_.each(tmpDataSource, function(item) {
    		if (item) {
    			var fval=_.isArray(item[field]) ? item[field][0] : item[field];
    			if (fieldType.toLowerCase()=="int") {
    				fval=parseInt(item[field]);
    				fval=isNaN(fval) ? 0 : fval;
    				item.__sortvar__=fval;
    			} else {
    				item.__sortvar__=fval;
    			}
    		}
    	});
    	//去掉空值
    	tmpDataSource=_.compact(tmpDataSource);
    	//对数据集进行排序
    	var resortData=_.sortBy(tmpDataSource, "__sortvar__");
    	if (order=="desc") {
    		resortData=resortData.reverse();
    	}
    	//去掉临时排序字段
    	resortData=_.map(resortData, function(item) { 
    		return _.omit(item, "__sortvar__"); 
    	});
    	//清空数据源，然后把新的排序后结果集添加到数据源中
    	this.removeAll();
    	var _me=this;
    	_.each(resortData, function(item) {
    		_me.add(item);
    	});
    	return this.data;
    }
});

//数据表格UI
smartforms.formctrl.UIDataTable = smartforms.formctrl.UIBaseCtrl.extend({
    init: function(tableId) {
        this.dataSource = new smartforms.formctrl.DataTableEntity(); 
        this.tableId=tableId;
        this.incNum=2;
        this.serialNum=false;
        this.tplRow=$("#"+this.tableId+" tbody tr:first").clone();
        //初始化第一行数据，获取控件名称
        var firstRow=this.tplRow.clone();
        var rowData=this._initRowCtrl(firstRow);
        this.dataSource.add(rowData);
        //生成序号列
        this.calcSerialNum();
        this._super(tableId);
    },

    addRow: function() {
        var newrow=this.tplRow.clone();
        this._renameRowCtrl(newrow);
        newrow.appendTo($("#"+this.tableId+" tbody"));
        //获取新行中的所有表单控件，获取控件name属性
        var rowData=this._initRowCtrl(newrow);
        this.dataSource.add(rowData);
        //重新计算序号
        this.calcSerialNum();
        //触发 onAddRow 事件
        var index=this.dataSource.getSize();
        $(this).trigger("onAddRow", [newrow, rowData, index-1, this.tableId]);
    },

    removeRow: function(index) {
        this.dataSource.remove(index);
        $("#"+this.tableId+" tbody tr:eq("+index+")").remove();
        //重新计算序号
        this.calcSerialNum();
        //触发 onRemoveRow 事件
        $(this).trigger("onRemoveRow");
    },

    removeAllRow: function() {
        this.dataSource.removeAll();
        $("#"+this.tableId+" tbody tr").remove();
        //触发 onRemoveAllRow 事件
        $(this).trigger("onRemoveAllRow");
    },

    getData: function() {
        var objData=this.dataSource.getAllData();
        var jsoStr=JSON.stringify(objData);
        return jsoStr;
    },

    getDataObj: function() {
        var objData=this.dataSource.getAllData();
        return objData;
    },

    setData: function(data) {
        this.dataSource.removeAll();
        for (var i=0; i<data.length; i++) {
            this.dataSource.add(data[i]);
        }
    },

    /**
     * 将数据渲染到数据表格中，一般用于初始化表格数据
     */
    renderRow: function(data) {
        var comfn=new smartforms.jsutils.CommonFn();
        //清除表格中现有的所有数据
        this.removeAllRow();

        for (var i=0; i<data.length; i++) {
            var tmpdata=data[i];
            this.dataSource.add(tmpdata);
            //添加新的数据行，然后把数据填入对应的控件中
            var newrow=this.tplRow.clone();
            for (var key in tmpdata) {
                //遍历克隆行，找到对应名称的控件填入数据
                var ctrls=$(newrow).find("[name="+key+"]");
                $.each(ctrls, function(index) {
                    var nodeName=this.nodeName.toLowerCase();
                    var tmpval=$(this).val();
                    if (nodeName=="input") {
                        var type=$(this).attr("type");
                      
                        type=(typeof(type)==="undefined") ? "text" : type.toLowerCase();
                     
                        if ("radio"==type || "checkbox"==type) {
                            //判断当前控件的值是否与数据源的值相等，相等的话就选中
                            if (comfn.isValMember(tmpval, tmpdata[key])) {
                                $(this).prop("checked", true);
                            }
                        } else if ("text"==type || "hidden"==type) {
                            $(this).val(tmpdata[key]);
                        }
                        
                        /* 获取class 属性的值 判断是否 textbox-value
                        如果是说明是easyui控件*/
                        var cl=$(this).attr("class");
                        if(cl=="textbox-value") {
                        	$(this).textbox('setValue',tmpdata[key]);
                        }
                    } else if (nodeName=="textarea") {
                        $(this).val(tmpdata[key]);
                    } else if (nodeName=="select") {
                        var options=$(this).children("option");
                        $.each(options, function(index, optItem) {
                            var tmpval=$(optItem).val();
                            //判断当前控件的值是否与数据源的值相等，相等的话就选中
                            if (comfn.isValMember(tmpval, tmpdata[key])) {
                                $(this).prop("selected", true);
                            }
                        });
                    }
                });
            }
            this._renameRowCtrl(newrow);
            //初始化的时候，判断是否设置为自动编辑，默认情况下，是将所有控件的状态都设置为disabled
            var autoEdit=$("#__autoEdit").val();
            if (autoEdit!="true") {
                $.each($(newrow).find(":input"), function(index) {
                    $(this).prop("disabled",true);
                });
            }
            newrow.appendTo($("#"+this.tableId+" tbody"));
        }

        //生成序号列
        this.calcSerialNum();

        //触发 afterRender 事件
        $(this).trigger("onAfterRender", this);
    },

    updateRow: function(index) {
        var trrow=$("#"+this.tableId+" tbody tr:eq("+index+")");
        var rowData=this.dataSource.get(index);
        var me=this;
        var inputCtrls=trrow.find("input");
        //将控件数组中的checkbox数组清空，然后再重新赋值
        var ckboxs=$(inputCtrls).filter("[type=checkbox]");
        $.each(ckboxs, function(index) {
            var name=$(this).attr("name");
            if ($.trim(name)!="") {
                name=me._restoreCtrlName(name);
                rowData[name]=[];
            }
        });
        //遍历input标签控件值进行复制
        $.each(inputCtrls, function(index) {
            var name=$(this).attr("name");
            if ($.trim(name)!="") {
                name=me._restoreCtrlName(name);
                var type=$(this).attr("type");
                type=(typeof(type)==="undefined") ? "text" : type.toLowerCase();
                if ("radio"==type) {
                    if ($(this).prop("checked")) {
                        rowData[name]=$(this).val();
                    }
                } else if ("checkbox"==type) {
                    if ($(this).prop("checked")) {
                        rowData[name].push($(this).val());
                    }
                } else if ("text"==type || "hidden"==type) {
                    rowData[name]=$(this).val();
                }
            }
        });
        //遍历textarea标签控件
        var taCtrls=trrow.find("textarea");
        $.each(taCtrls, function(index) {
            var name=$(this).attr("name");
            if ($.trim(name)!="") {
                name=me._restoreCtrlName(name);
                rowData[name]=$(this).val();
            }
        });
        //将控件数组中的select数组清空，然后再重新赋值
        var selectCtrls=trrow.find("select");
        $.each(selectCtrls, function(index) {
            var name=$(this).attr("name");
            if ($.trim(name)!="") {
                name=me._restoreCtrlName(name);
                rowData[name]=[];
            }
        });
        //遍历select标签控件进行赋值
        $.each(selectCtrls, function(index) {
            var name=$(this).attr("name");
            if ($.trim(name)!="") {
                name=me._restoreCtrlName(name);
                var options=$(this).children("option:selected");
                for (var i=0; i<options.length; i++) {
                    rowData[name].push($(options[i]).val());
                }
            }
        });
        //触发 onUpdateRow 事件
        $(this).trigger("onUpdateRow", [trrow, rowData]);
    },

    insertRow: function(index) {
        var newrow=this.tplRow.clone();
        var rowsObj=$("#"+this.tableId+" tbody tr");
        //判断插入的下标是否超过列表长度，若超过，则改为添加行
        if (rowsObj && index>=rowsObj.length) {
            //添加行
            this.addRow();
        } else {
            //插入行
            this._renameRowCtrl(newrow);
            newrow.insertBefore($("#"+this.tableId+" tbody tr:eq("+index+")"));
            //获取新行中的所有表单控件，获取控件name属性
            var rowData=this._initRowCtrl(newrow);
            this.dataSource.insert(rowData, index);
            //重新计算序号
            this.calcSerialNum();
            //触发 onInsertRow 事件
            $(this).trigger("onInsertRow", [newrow, rowData, index, this.tableId]);
        }
    },

    /**
     * 获取指定行中的所有表单控件名称，并且初始化值
     */
    _initRowCtrl: function(row) {
        var rowData={};
        var me=this;
        //遍历input标签控件
        var inputCtrls=row.find("input");
        $.each(inputCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取input标签的控件名称
            if (nodeType==1 && (nodeName=="input")) {
                var name=$(item).attr("name");
                var type=$(item).attr("type");
                type=(typeof(type)==="undefined") ? "text" : type.toLowerCase();
                if ($.trim(name)!="") {
                    name=me._restoreCtrlName(name);
                    //判断控件类型是复选框的话，缺省值为空数组
                    if ("checkbox"==type) {
                        rowData[name]=[];
                    } else {
                        rowData[name]="";
                    }
                }
            }
        });
        //遍历textarea标签控件
        var taCtrls=row.find("textarea");
        $.each(taCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取多行文本框控件名称
            if (nodeType==1 && (nodeName=="textarea")) {
                var name=$(item).attr("name");
                if ($.trim(name)!="") {
                    name=me._restoreCtrlName(name);
                    rowData[name]="";
                }
            }
        });
        //遍历select标签控件
        var selectCtrls=row.find("select");
        $.each(selectCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取下拉框或列表框控件名称
            if (nodeType==1 && (nodeName=="select")) {
                var name=$(item).attr("name");
                if ($.trim(name)!="") {
                    name=me._restoreCtrlName(name);
                    rowData[name]=[];
                }
            }
        });
        return rowData;
    },

    /**
     * 给指定数据行中的每个控件加上序号
     */
    _renameRowCtrl: function(row) {
        var inputCtrls=row.find("input");
        //遍历所有input标签控件
        var incNum=this.incNum;
        $.each(inputCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取input标签的控件名称
            if (nodeType==1 && (nodeName=="input")) {
                var name=$(item).attr("name");
                if ($.trim(name)!="") {
                    var pos=name.lastIndexOf("＃");
                    if (pos<0) {
                        $(item).attr("name",name+"＃"+incNum);
                    } else {
                        $(item).attr("name",name.substr(pos)+"＃"+incNum);
                    }
                }
            }
        });
        //遍历所有textarea标签控件
        var taCtrls=row.find("textarea");
        $.each(taCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取多行文本框控件名称
            if (nodeType==1 && (nodeName=="textarea")) {
                var name=$(item).attr("name");
                if ($.trim(name)!="") {
                    var pos=name.lastIndexOf("＃");
                    if (pos<0) {
                        $(item).attr("name",name+"＃"+incNum);
                    } else {
                        $(item).attr("name",name.substr(pos)+"＃"+incNum);
                    }
                }
            }
        });
        //遍历所有select标签控件
        var selectCtrls=row.find("select");
        $.each(selectCtrls, function(index, item) {
            var nodeType=item.nodeType;
            var nodeName=item.nodeName.toLowerCase();
            //获取下拉框或列表框控件名称
            if (nodeType==1 && (nodeName=="select")) {
                var name=$(item).attr("name");
                var pos=name.lastIndexOf("＃");
                if ($.trim(name)!="") {
                    var pos=name.lastIndexOf("＃");
                    if (pos<0) {
                        $(item).attr("name",name+"＃"+incNum);
                    } else {
                        $(item).attr("name",name.substr(pos)+"＃"+incNum);
                    }
                }
            }
        });
        this.incNum++;
    },

    /**
     * 还原控件名称，去掉控件名称的序号。
     */
    _restoreCtrlName: function(name) {
        var retname=name;
        if ($.trim(retname)!="") {
            var pos=retname.lastIndexOf("＃");
            if (pos>0) {
                retname=retname.substr(0,pos);
            }
        }
        return retname;
    },

    editRow: function(index) {
        //找到指定表格行中的所有input控件
        $.each($("#"+this.tableId+" tbody tr:eq("+index+") :input"), function(index) {
            $(this).prop("disabled",false);
        });
        //触发 onEditRow 事件
        var rowObj=$("#"+this.tableId+" tbody tr:eq("+index+")");
        $(this).trigger("onEditRow", rowObj);
    },

    /**
     * 计算序号并生成序号列
     */
    calcSerialNum: function() {
        var showSerialNum=$("#__showSerialNum").val();
        if (showSerialNum) {
            //找到序号列，然后自动生成序号
            var snum=1;
            var serialTds=$("#"+this.tableId+" tbody td.serialnum");
            if (serialTds.length>0) {
                $.each(serialTds, function(index, item) {
                    $(item).html(snum);
                    snum++;
                });
            }
        }
    }
});
