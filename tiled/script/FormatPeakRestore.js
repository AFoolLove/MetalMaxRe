// author AFoolLove
// tiled version 1.7.2
//
// emm 可以恢复 FormatPeak 修改的地图，不然魔法棒和相同图块选中什么的不好选中
var action = tiled.registerAction("FormatPeakRestore", function(action){
    // 得到当前地图
    var world = tiled.activeAsset;
    if(world == null || !world.isTileMap){
        // 无效或未选中地图
         tiled.alert("请选中需要恢复被格式化的地图！")
        return;
    }
    for(var i = 0; i < world.layerCount; i++){
        // 获取到 "world" 层
        var layer = world.layerAt(i);
        if (layer.isTileLayer && layer.name == "world"){
            // 获取该层的编辑器
            var editLayer = layer.edit();

            for(var index = 0; index < (0x100 * 0x100); index++){
                var x = index % 0x100;
                var y = index / 0x100;

                var tile = layer.tileAt(x, y);
                if(tile == null){
                    continue;
                }
                // 对，无脑设置为 0x02就行
                if(tile.id == 0x0A || tile.id == 0x0B || tile.id == 0x03){
                    editLayer.setTile(x, y, tile.tileset.tile(0x02));
                }
            }
            editLayer.apply();
            print("Format Peak Restore OK.")
            break;
        }
    }
})
action.text = "Format Peak Restore"

tiled.extendMenu("Map", [
    {action:"FormatPeakRestore"}
])