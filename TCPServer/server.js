/**
 * Created by daddyslab on 2016. 11. 27..
 */
var net = require('net');
var mongoose = require('mongoose');

mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/iot', function (err) {
    if (err) {
        console.log("DB ERROR :", err);
        throw err;
    }
    else
        console.log("DB connected!");
});

require('./model.js');
var Data = mongoose.model('Data');

var server = net.createServer(function (client) {
    console.log('Client connection: ');
    console.log('   local = %s:%s', client.localAddress, client.localPort);
    console.log('   remote = %s:%s', client.remoteAddress, client.remotePort);
    client.setTimeout(500);
    client.setEncoding('utf8');
    client.on('data', function (data) {
        var result = data.toString();
        console.log('Received data from client on port %d: %s',
            client.remotePort, data.toString());
        console.log('  Bytes received: ' + client.bytesRead);
        Data.find().exec(function (err, data) {
            if (data) {
                var d = data[0];
                if(result.contains("a"))
                    d.pos1 = true;
                else if(result.contains("b"))
                    d.pos1 = false;
                if(result.contains("c"))
                    d.pos2 = true;
                else if(result.contains("d"))
                    d.pos2 = false;
                if(result.contains("e"))
                    d.pos3 = true;
                else if(result.contains("f"))
                    d.pos3 = false;
                if(result.contains("g"))
                    d.pos4 = true;
                else if(result.contains("h"))
                    d.pos5 = false;
                d.save();
            }
        })
    });
    client.on('end', function () {
        console.log('Client disconnected');
        server.getConnections(function (err, count) {
            console.log('Remaining Connections: ' + count);
        });
    });
    client.on('error', function (err) {
        console.log('Socket Error: ', JSON.stringify(err));
    });
    client.on('timeout', function () {
        console.log('Socket Timed out');
    });
});
server.listen(8000, function () {
    console.log('Server listening: ' + JSON.stringify(server.address()));
    server.on('close', function () {
        console.log('Server Terminated');
    });
    server.on('error', function (err) {
        console.log('Server Error: ', JSON.stringify(err));
    });
});
function writeData(socket, data) {
    var success = !socket.write(data);
    if (!success) {
        (function (socket, data) {
            socket.once('drain', function () {
                writeData(socket, data);
            });
        })(socket, data);
    }
}