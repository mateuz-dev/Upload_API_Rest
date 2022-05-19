const express = require('express');
const fs = require('fs');

const app = express();

app.use(express.json());
app.use(express.urlencoded({extended:true, limit: '5MB'}));

app.post('/testeUpload', (req, res)=>{
    let buffer = new Buffer.from(req.body.file, 'base64');
    let imageName = './uploads/' + Date.now().toString() + '.jpg';
    
    fs.writeFileSync(imageName, buffer, 'base64', (error) => {
        if(error) console.log(error);
    });

    res.status(200)
});


app.listen(3000, ()=>{
    console.log('SERVIDOR RODANDO EM http://localhohost:3000');
});