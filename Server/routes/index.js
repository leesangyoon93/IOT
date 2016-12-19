var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();
var Data = mongoose.model('Data');

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'IOT Server'});
});

router.post('/getData', function (req, res) {
    Data.find().exec(function (err, data) {
        if (err) return res.json({'result': 'fail'});
        if (data) {
            return res.json(data[0]);
        }
        else return res.json({'result': 'fail'});
    })
});

router.post('/getDistance', function (req, res) {
    var latitude1 = parseFloat(req.body.latitude);
    var longtitude1 = parseFloat(req.body.longtitude);
    var distance = calDistance(37.2811028, 127.0507571, latitude1, longtitude1);
    return res.json({'result': parseInt(distance)});
});

router.post('/updateData', function (req, res) {
    console.log(req.body)
    Data.find().exec(function (err, data) {
        if (err) return res.json({'result': 'fail'});
        if (data) {
            console.log(data);
            carNum = req.body.carNumber;
            state = req.body.state;
            if (carNum == "1") data[0].pos1 = !state;
            if (carNum == "2") data[0].pos2 = !state;
            if (carNum == "3") data[0].pos3 = !state;
            if (carNum == "4") data[0].pos4 = !state;
            data[0].save();
            return res.json({'result': 'success'})
        }
        else return res.json({'result': 'fail'});
    })

})

function calDistance(lat1, lon1, lat2, lon2) {

    var theta, dist;
    theta = lon1 - lon2;
    dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
        * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);

    dist = dist * 60 * 1.1515;
    dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
    dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

    return dist;
}

// 주어진 도(degree) 값을 라디언으로 변환
function deg2rad(deg) {
    return parseFloat(deg * Math.PI / parseFloat(180));
}

// 주어진 라디언(radian) 값을 도(degree) 값으로 변환
function rad2deg(rad) {
    return parseFloat(rad * parseFloat(180) / Math.PI);
}

module.exports = router;
