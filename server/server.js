var express = require("express")();
var http = require("http").createServer(express);
var io = require("socket.io")(http);

// express.get("/" , function (req , res , next) {
//     console.log(req.headers);
//     res.end("Server Connected");
// })

io.on("connection" , function (socket) {
    console.log("user :"+ socket.id);
    socket.on("msg" , function (data) {
        var sockets = io.sockets.sockets;
        sockets.forEach(function (item) {
            if (item.id != socket.id)
                item.emit("msg", data);
            else
                item.emit("msg", "me :"+data);
        });
    });
    socket.on("disconnect" , function () {
       console.log("a user disconnect with ID :"+socket.id);
    });

});


http.listen(8000);
console.log("Server is running.")