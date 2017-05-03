//数据表格V2 UI
smartforms.formctrl.UIDataTableV2 = smartforms.formctrl.UIDataTable.extend({
    init: function(tableId, autoIncreaseId, dsTable, autoEdit) {
    	this._super(tableId);
        this.dataSource = new smartforms.formctrl.DataTableEntity(); 
        this.tableId=tableId;
        this.autoIncreaseId=autoIncreaseId;
        this.autoEdit=autoEdit;
        this.incNum=2;
        //获取表格数据中行ID的最大值，然后加1作为数据源开始下标
        var maxIdx=0;
        if (dsTable.length>0) {
        	$.each(dsTable, function() {
        		var rowgrpId=this.__rowgrpId;
        		var regex=/\w+_\d+/i;
        		if ((typeof rowgrpId!="undefined") && regex.test(rowgrpId)) {
        			var pos=rowgrpId.lastIndexOf("_");
        			var num=rowgrpId.substring(pos+1);
        			num=parseInt(num);
                    if (num>maxIdx) {
                    	maxIdx=num;
                    }
        		}
        	});
        }
        this.rowgrpIndex=maxIdx+1;
        //this.serialNum=false;
        this.tplRow=$("#"+this.tableId+" tbody >");
        //初始化第一行数据，初始化控件名称
        var firstRow=this.tplRow.clone();
        var rowgrpId=this._setRowGroup(firstRow);
        var rowData=this._initRowCtrl(firstRow);
        rowData.__rowgrpId=rowgrpId;
        this.dataSource.add(rowData);
        //this._bindRowGrpId(firstRow, rowgrpId);
        $("#"+this.tableId+" tbody").empty();
        $("#"+this.tableId+" tbody").append(firstRow);
        //生成序号列
        //this.calcSerialNum();
    },
    
    addRow: function(dataObj) {
        var newrow=this.tplRow.clone();
        this._renameRowCtrl(newrow);
        var rowgrpId=this._setRowGroup(newrow);
        this._bindRowGrpId(newrow, rowgrpId);
        newrow.appendTo($("#"+this.tableId+" tbody"));
        //获取新行中的所有表单控件，获取控件name属性
        var rowData=this._initRowCtrl(newrow);
        rowData.__rowgrpId=rowgrpId;
        //this._bindRowGrpId(newrow, rowgrpId);
        this.dataSource.add(rowData);
        //重新计算序号
        //this.calcSerialNum();
        //触发 onAddRow 事件
        $(this).trigger("onAddRow", [newrow, rowData, rowgrpId, this.tableId, dataObj]);
        return newrow;
    },
        
    removeRow: function(rowgrpId) {
        var row=$("#"+this.tableId+" tbody [rowgroup="+rowgrpId+"]");
        if (row.length>0) {
        	var elm=this.dataSource.removeByKey("__rowgrpId", rowgrpId);
        	if (elm!=null) {
        		$(row).remove();
        	}
        }
        //重新计算序号
        //this.calcSerialNum();
        //触发 onRemoveRow 事件
        $(this).trigger("onRemoveRow");
        this.storeData();
    },
    
    removeAllRow: function() {
        this.dataSource.removeAll();
        $("#"+this.tableId+" tbody").empty();
        this.dataSource.removeAll();
        //触发 onRemoveAllRow 事件
        $(this).trigger("onRemoveAllRow");
        this.storeData();
    },
    
    updateRow: function(rowgrpId) {
        var trrow=$("#"+this.tableId+" tbody tr[rowgroup="+rowgrpId+"]");
        var rowData=this.dataSource.find("__rowgrpId", rowgrpId);
        if (typeof rowData != "undefined") {
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
        }
        //触发 onUpdateRow 事件
        $(this).trigger("onUpdateRow", [trrow, rowData]);
        this.storeData();
    },
    
    insertRow: function(rowgrpId, dataObj) {
        var newrow=this.tplRow.clone();
        rowgrpId=((typeof rowgrpId=="undefined") || (rowgrpId=="")) ? "none" : rowgrpId; 
        var rowsObj=$("#"+this.tableId+" tbody tr[rowgroup="+rowgrpId+"]");
        //判断是否存在指定行，若不存在，则添加行
        if (rowsObj.length<=0) {
            //添加行
            newrow=this.addRow(dataObj);
        } else {
            //插入行
            this._renameRowCtrl(newrow);
            //获取新行中的所有表单控件，获取控件name属性
            var rowData=this._initRowCtrl(newrow);
            var newRowgrpId=this._setRowGroup(newrow);
            this._bindRowGrpId(newrow, newRowgrpId);
            rowData.__rowgrpId=newRowgrpId;
            //this._bindRowGrpId(newrow, rowgrpId);
            var rowobj=this.dataSource.insertByKey(rowData, "__rowgrpId", rowgrpId);
            if (rowobj!=null) {
            	newrow.insertBefore($("#"+this.tableId+" tbody tr[rowgroup="+rowgrpId+"]:first"));
            }
            //重新计算序号
            //this.calcSerialNum();
            //触发 onInsertRow 事件
            $(this).trigger("onInsertRow", [newrow, rowData, newRowgrpId, this.tableId, dataObj]);
        }
        return newrow;
    },
    
    editRow: function(rowgrpId) {
        //找到指定表格行中的所有input控件
        $.each($("#"+this.tableId+" tbody tr[rowgroup="+rowgrpId+"] :input"), function(index) {
            $(this).prop("disabled",false);
        });
        //触发 onEditRow 事件
        var rowObj=$("#"+this.tableId+" tbody tr:eq("+index+")");
        $(this).trigger("onEditRow", rowObj);
    },
    
    editAllRow: function() {
        //找到指定表格行中的所有input控件
        $.each($("#"+this.tableId+" tbody tr[rowgroup] :input"), function(index) {
            $(this).prop("disabled",false);
        });
    },

    getData: function() {
    	return this._super();
    },

    getDataObj: function() {
    	return this._super();
    },

    setData: function(data) {
    	this._super(data);
    },
    
    storeData: function() {
    	var dataStr=this.getData();
    	$("#__dyntab_v2_data__"+this.tableId).val(dataStr);
    },
    
    _setRowGroup: function(row) {
    	//遍历第一层tr，然后分配一个组名
    	var rowgrpIdx=this.rowgrpIndex;
    	var rowgrpId="rowgrp_"+rowgrpIdx;
        $.each(row, function(index, item) {
        	$(this).attr("rowgroup", rowgrpId);
        });
        this.rowgrpIndex++;
        return rowgrpId;
    },
    
    /**
     * 获取指定行中的所有表单控件名称，并且初始化值
     */
    _initRowCtrl: function(row) {
    	return this._super(row);
    },
    
    /**
     * 给指定数据行中的每个控件的Name属性加上序号
     */
    _renameRowCtrl: function(row) {
    	this._super(row);
    	this._increaseId(row);
    },

    /**
     * 还原控件名称，去掉控件名称的序号。
     */
    _restoreCtrlName: function(name) {
    	return this._super(name);
    },
    
    /**
     * 将数据渲染到数据表格中，一般用于初始化表格数据
     */
    renderRow: function(data) {
        var comfn=new smartforms.jsutils.CommonFn();
        //清除表格中现有的所有数据
        this.removeAllRow();
        //将表格数据源存放到隐藏域中
        if (data.length>0) {
        	var jstr=JSON.stringify(data);
        	$("#__dyntab_v2_data__"+this.tableId).val(jstr);
        }

        for (var i=0; i<data.length; i++) {
            var tmpdata=data[i];
            if (!tmpdata) continue;
            var rowgrpId=tmpdata.__rowgrpId;
            this.dataSource.add(tmpdata);
            //添加新的数据行，然后把数据填入对应的控件中
            var newrow=this.tplRow.clone();
            //将占位符替换为行组ID
            this._bindRowGrpId(newrow, rowgrpId);
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
                            //判断当前控件的值是否与数据源的值相等并且不是空值，相等的话就选中
                            if (comfn.isValMember(tmpval, tmpdata[key]) 
                            		&& $.trim(tmpval)!="") {
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
            //给每行加上行组ID
            $.each(newrow, function() {
            	$(this).attr("rowgroup", rowgrpId);
            });
            //初始化的时候，判断是否设置为自动编辑，默认情况下，是将所有控件的状态都设置为disabled
            if (this.autoEdit!="enabled") {
                $.each($(newrow).find(":input"), function(index) {
                    $(this).prop("disabled",true);
                });
            }
            newrow.appendTo($("#"+this.tableId+" tbody"));
        }

        //生成序号列
        //this.calcSerialNum();

        //触发 afterRender 事件
        $(this).trigger("onAfterRender", this);
    },
    
    /**
     * 给行中的所有自增ID的元素更新ID值
     */
    _increaseId: function(row) {
    	if (this.autoIncreaseId=="enabled") {
    		var inputCtrls=row.find(":input");
        	var regex=/\w+_\d+/i;
            //遍历所有input标签控件
            var incNum=this.incNum;
            $.each(inputCtrls, function(index, item) {
                var nodeType=item.nodeType;
                var nodeName=item.nodeName.toLowerCase();
                if (nodeType==1) {
                	var id=$(item).attr("id");
                    if ($.trim(id)!="" && regex.test(id)) {
                    	var pos=id.lastIndexOf("_");
                        id=id.substring(0, pos+1)+(incNum-1);
                        $(item).attr("id", id);
                    }
                }
            });
    	}
    },
    
    /**
     * 在行中找到{{:rowgrpId}}的值绑定，然后替换为行组ID值
     */
    _bindRowGrpId: function(row, rowgrpId) {
    	$.each(row, function() {
    		var html=$(this).html();
    		html=html.replace(/{{:rowgrpId}}/g, rowgrpId);
    		$(this).html(html);
    	});
    },
    
    setRowData: function(row, data) {
    	var comfn=new smartforms.jsutils.CommonFn();
    	var rowgrpId=$(row).first().attr("rowgroup");
    	var rowData=this.dataSource.find("__rowgrpId", rowgrpId);
    	if ((typeof rowData!="undefined") && rowData) {
    		$.each(data, function(dataKey, dataVal) {
    			//设置行数据源的值
    			if (rowData.hasOwnProperty(dataKey)) {
    				rowData[dataKey]=dataVal;
    			}
    			//查找input标签控件
        		var ctrls=$(row).find(":input[name^="+dataKey+"]");
        		$.each(ctrls, function(index) {
        			var tagName=$(this).prop("tagName");
        			var tagName=tagName.toLowerCase();
        			if (tagName=="input") {
        				var type=$(this).attr("type");
                        type=(typeof(type)==="undefined") ? "text" : type.toLowerCase();
                        if ("radio"==type || "checkbox"==type) {
                        	var tmpval=$(this).val();
                            //判断当前控件的值是否与数据源的值相等并且不是空值，相等的话就选中
                        	if (comfn.isValMember(tmpval, dataVal) 
                            		&& $.trim(tmpval)!="") {
                                $(this).prop("checked", true);
                            } else {
                            	$(this).prop("checked", false);
                            }
                        } else if ("text"==type || "hidden"==type) {
                        	if ($.isArray(dataVal)) {
                        		$(this).val(dataVal[0]);
                        	} else {
                        		$(this).val(dataVal);
                        	}
                        }
        			} else if (tagName=="textarea") {
        				if ($.isArray(dataVal)) {
                    		$(this).val(dataVal[0]);
                    	} else {
                    		$(this).val(dataVal);
                    	}
                    } else if (tagName=="select") {
                        var options=$(this).children("option");
                        $.each(options, function(index, optItem) {
                            var tmpval=$(optItem).val();
                            //判断当前控件的值是否与数据源的值相等，相等的话就选中
                            if (comfn.isValMember(tmpval, dataVal) 
                            		&& $.trim(tmpval)!="") {
                                $(this).prop("checked", true);
                            } else {
                            	$(this).prop("checked", false);
                            }
                        });
                    }
        		});
        	});
    	}
    },
    
    sortData: function(field, fieldType, order) {
    	fieldType=(fieldType.toLowerCase()=="int") ? "int" : "string";
    	order=(order.toLowerCase()=="desc") ? "desc" : "asc";
    	var ds=this.dataSource.sortBy(field, fieldType, order);
    	if (ds) {
    		this.renderRow(ds);
    	}
    }
});