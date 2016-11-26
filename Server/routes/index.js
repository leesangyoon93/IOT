var express = require('express');
var mongoose = require('mongoose');
var router = express.Router();
var Data = mongoose.model('Data');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'IOT Server' });
});

router.post('/getData', function(req, res) {
  Data.find().exec(function(err, data) {
    if(err) return res.json({'result': 'fail'});
    if(data) return res.json(data[0]);
    else return res.json({'result': 'fail'});
  })
});

module.exports = router;
