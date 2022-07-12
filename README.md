The backend server with google cloud project ID: artsy-server:

1. https://artsy-server.wl.r.appspot.com/centaurus/artist_list/ + userInput

 -- []: {name : ... , id : ... , picURL : ... }		

2. https://artsy-server.wl.r.appspot.com/centaurus/artist_info/ + artist_id

 -- {name : ... , birthday : ... , deathday : ... , nationality : ... , bio : ... }

3. https://artsy-server.wl.r.appspot.com/centaurus/artwork_list/ + artist_id

 -- []: {artwork_id : ... , artwork_name : ... , artwork_date : ... , artwork_picURL : ... };

4. https://artsy-server.wl.r.appspot.com/centaurus/gene/ + artwork_id

 -- []: {gene_name : ... , gene_picURL : ... , gene_description : ... }
