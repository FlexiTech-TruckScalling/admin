// A $( document ).ready() block.

  var tree = $('#tree').tree({
    primaryKey: 'shotName',
    uiLibrary: 'bootstrap4',
    //uiLibrary: 'materialdesign',
    dataSource: contextPath+ '/get_tree_json.json',
    checkboxes: true,
    cascadeCheck: true
	});

$( document ).ready(function() {
    console.log( "ready!" );
    var roleId = document.getElementById('roleId').value;
	tree.reload({ 'roleId': roleId});
	console.log("js before..."+document.getElementById('accessTreeList').value);
	$('#btnSubmit').on("click", function(){
		console.log(tree.getCheckedNodes());
		document.getElementById('accessTreeList').value = tree.getCheckedNodes();
		return true;
	});
	console.log("js after..."+document.getElementById('accessTreeList').value);
	$('#roleId').on("change", function(){
		var roleId = document.getElementById('roleId').value;
		console.log("tree reload"+ roleId)
		tree.reload({ 'roleId': roleId});
	});
});