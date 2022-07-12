The backend server with google cloud project ID: artsy-server:

https://artsy-server.wl.r.appspot.com/centaurus/artist_list/ + userInput

 -- []: {name : ... , id : ... , picURL : ... }		

https://artsy-server.wl.r.appspot.com/centaurus/artist_info/ + artist_id

 -- {name : ... , birthday : ... , deathday : ... , nationality : ... , bio : ... }

https://artsy-server.wl.r.appspot.com/centaurus/artwork_list/ + artist_id

 -- []: {artwork_id : ... , artwork_name : ... , artwork_date : ... , artwork_picURL : ... };

https://artsy-server.wl.r.appspot.com/centaurus/gene/ + artwork_id

 -- []: {gene_name : ... , gene_picURL : ... , gene_description : ... }
