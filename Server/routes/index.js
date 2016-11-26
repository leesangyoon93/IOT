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

router.post('/updateData', function(req, res){
  console.log(req.body);
  return res.json({'result': 'success'});
  // Data.find().exec(function(err, data) {
  //   if(err) return res.json({'result': 'fail'});
  //   if(data) {
  //     var d = data[0];
  //     d.pos1 = req.body.pos1;
  //     d.pos2 = req.body.pos2;
  //     d.pos3 = req.body.pos3;
  //     d.pos4 = req.body.pos4;
  //     d.save();
  //     return res.json({'result': 'success'})
  //   }
  //   else return res.json({'result': 'fail'});
  // })
});

module.exports = router;
