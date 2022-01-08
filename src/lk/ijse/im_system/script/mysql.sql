DROP DATABASE IF EXISTS IMS;
CREATE DATABASE IF NOT EXISTS IMS;
SHOW DATABASES;
USE IMS;

#-----------------------------------4---------------------------------------
DROP TABLE IF EXISTS InchargeLogin;
CREATE TABLE IF NOT EXISTS InchargeLogin(
    userName VARCHAR(30) NOT NULL,
    userPassword VARCHAR(20) NOT NULL,
    userType VARCHAR(20),
    wardNo VARCHAR(8),
    email VARCHAR(50),

    CONSTRAINT PRIMARY KEY (userName),
    CONSTRAINT FOREIGN KEY (wardNo) REFERENCES Ward(wardNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE InchargeLogin;

ALTER TABLE InchargeLogin ADD emailPassword VARCHAR(20);
ALTER TABLE InchargeLogin MODIFY COLUMN userType ENUM('INCHARGE','STOREKEEPER');
ALTER TABLE InchargeLogin MODIFY COLUMN userPassword VARCHAR(160);
ALTER TABLE InchargeLogin MODIFY COLUMN emailPassword VARCHAR(160);

#------------------------------3----------------------------------------------

DROP TABLE IF EXISTS StorekeeperLogin;
CREATE TABLE IF NOT EXISTS StorekeeperLogin(
    userName VARCHAR(30) NOT NULL,
    userPassword VARCHAR(20) NOT NULL,
    userType VARCHAR(20),

    CONSTRAINT PRIMARY KEY (userName)
);
SHOW TABLES ;
DESCRIBE StorekeeperLogin;

/*ALTER TABLE StorekeeperLogin ADD email VARCHAR(50);
ALTER TABLE StorekeeperLogin ADD emailPassword VARCHAR(20);*/
ALTER TABLE StorekeeperLogin MODIFY COLUMN userType ENUM('INCHARGE','STOREKEEPER');
ALTER TABLE StorekeeperLogin MODIFY COLUMN userPassword VARCHAR(160);

/*ALTER TABLE StorekeeperLogin MODIFY COLUMN userPassword BLOB;*/
/*ALTER TABLE StorekeeperLogin MODIFY COLUMN userPassword VARBINARY(160);*/

#----------------------------------1------------------------------------------

DROP TABLE IF EXISTS WardIncharge;
CREATE TABLE IF NOT EXISTS WardIncharge(
   inchargeId VARCHAR(8) NOT NULL,
   inchargeName VARCHAR(50),

   CONSTRAINT PRIMARY KEY (inchargeId)
);
SHOW TABLES ;
DESCRIBE WardIncharge;

#-----------------------------------------2---------------------

DROP TABLE IF EXISTS Ward;
CREATE TABLE IF NOT EXISTS Ward(
    wardNo VARCHAR(8) NOT NULL,
    title VARCHAR(100),
    extensionNo INT(10),
    inchargeId VARCHAR(8),

    CONSTRAINT PRIMARY KEY (wardNo),
    CONSTRAINT FOREIGN KEY (inchargeId) REFERENCES WardIncharge(inchargeId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE Ward;

#------------------------------------------5--------------------

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    orderId VARCHAR(8) NOT NULL,
    orderDate DATE,
    wardNo VARCHAR(8),

    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (wardNo) REFERENCES Ward(wardNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE Orders;

#---------------------------------9-----------------------------

DROP TABLE IF EXISTS OrderDetail;
CREATE TABLE IF NOT EXISTS OrderDetail(
    orderId VARCHAR(8) NOT NULL,
    invNo VARCHAR(8) NOT NULL,
    orderQTY INT(10),

    CONSTRAINT PRIMARY KEY (orderId,invNo),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (invNo) REFERENCES Inventory(invNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE OrderDetail;

#-------------------------------8-------------------------------

DROP TABLE IF EXISTS Inventory;
CREATE TABLE IF NOT EXISTS Inventory(
    invNo VARCHAR(8) NOT NULL,
    description VARCHAR(50),
    qtyOnHand INT(10),
    categoryId VARCHAR(8),
    locationId VARCHAR(8),

    CONSTRAINT PRIMARY KEY (invNo),
    CONSTRAINT FOREIGN KEY (categoryId) REFERENCES Category(categoryId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (locationId) REFERENCES Location(locationId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE Inventory;

ALTER TABLE Inventory ADD notifyLevel INT(10);

#----------------------------------6----------------------------

DROP TABLE IF EXISTS Category;
CREATE TABLE IF NOT EXISTS Category(
    categoryId VARCHAR(8) NOT NULL,
    categoryTitle VARCHAR(100),

    CONSTRAINT PRIMARY KEY (categoryId)
);
SHOW TABLES ;
DESCRIBE Category;

#----------------------------------------7----------------------

DROP TABLE IF EXISTS Location;
CREATE TABLE IF NOT EXISTS Location(
    locationId VARCHAR(8) NOT NULL,
    locationName VARCHAR(100),

    CONSTRAINT PRIMARY KEY (locationId)
);
SHOW TABLES ;
DESCRIBE Location;

ALTER TABLE Location ADD createdBy VARCHAR(20);

#----------------------------------------10----------------------

DROP TABLE IF EXISTS InventoryWithWard;
CREATE TABLE IF NOT EXISTS InventoryWithWard(
    wardNo VARCHAR(8) NOT NULL,
    invNo VARCHAR(8) NOT NULL,
    beforeQty INT(10),
    afterQty INT(10),
    lastModified DATE,
    location VARCHAR(50),

    CONSTRAINT PRIMARY KEY (wardNo,invNo),
    CONSTRAINT FOREIGN KEY (wardNo) REFERENCES Ward(wardNo) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (invNo) REFERENCES Inventory(invNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE InventoryWithWard;

ALTER TABLE InventoryWithWard ADD notifyLevel INT(10);

#------------------------------------------11--------------------

DROP TABLE IF EXISTS Condemnation;
CREATE TABLE IF NOT EXISTS Condemnation(
    condemnId VARCHAR(8) NOT NULL,
    wardNo VARCHAR(8),
    condemnDate DATE,

    CONSTRAINT PRIMARY KEY (condemnId),
    CONSTRAINT FOREIGN KEY (wardNo) REFERENCES Ward(wardNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE Condemnation;

#------------------------------12--------------------------------


DROP TABLE IF EXISTS CondemnDetail;
CREATE TABLE IF NOT EXISTS CondemnDetail(
    condemnId VARCHAR(8) NOT NULL,
    invNo VARCHAR(8) NOT NULL,
    condemnQTY INT(10),

    CONSTRAINT PRIMARY KEY (condemnId,invNo),
    CONSTRAINT FOREIGN KEY (condemnId) REFERENCES Condemnation(condemnId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (invNo) REFERENCES Inventory(invNo) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES ;
DESCRIBE CondemnDetail;

#--------------------------------13------------------------------

DROP TABLE IF EXISTS CondemnedInventory;
CREATE TABLE IF NOT EXISTS CondemnedInventory(
    invNo VARCHAR(8) NOT NULL,
    description VARCHAR(50),
    totalQtyCondemned INT(10),

    CONSTRAINT PRIMARY KEY (invNo)

);
SHOW TABLES ;
DESCRIBE CondemnedInventory;

#----------------------1---------------------------------------------

INSERT INTO WardIncharge VALUES ('ING-001','Jagath Perera'),
                                ('ING-002','Priyan Fernando'),
                                ('ING-003','Ramani Abeywickrama'),
                                ('ING-004','Sarath Gunawardane');

SELECT  * FROM WardIncharge;

#-----------------------------------------2--------------------------

INSERT INTO Ward VALUES ('WD-001','Medical Ward','2001','ING-001'),
                        ('WD-002','Medical Ward','2002','ING-002'),
                        ('WD-003','Surgical Ward','2003','ING-003'),
                        ('WD-004','Surgical Ward','2004','ING-004');
SELECT  * FROM Ward;

#---------------------------------------------3----------------------
INSERT INTO InchargeLogin VALUES
                        ('Incharge1','thkWard01','INCHARGE','WD-001','wd01@gmail.com','ward01@thk'),
                        ('Incharge2','thkWard02','INCHARGE','WD-002','wd02@gmail.com','ward02@thk'),
                        ('Incharge3','thkWard03','INCHARGE','WD-003','wd03@gmail.com','ward03@thk');

SELECT  * FROM InchargeLogin;
UPDATE InchargeLogin SET email = 'thkwd01@gmail.com' WHERE userName = 'Incharge1';
UPDATE InchargeLogin SET email = 'thkwd02@gmail.com' WHERE userName = 'Incharge2';
UPDATE InchargeLogin SET email = 'thkwd03@gmail.com' WHERE userName = 'Incharge3';
DELETE FROM InchargeLogin;


UPDATE InchargeLogin SET userPassword = SHA1('thkWard01') WHERE userPassword = 'thkWard01';
UPDATE InchargeLogin SET userPassword = SHA1('thkWard02') WHERE userPassword = 'thkWard02';

UPDATE InchargeLogin SET emailPassword = SHA1('ward01@thk') WHERE emailPassword = 'ward01@thk';
UPDATE InchargeLogin SET emailPassword = SHA1('ward02@thk') WHERE emailPassword = 'ward02@thk';

#--------------------------------4-----------------------------------

INSERT INTO StorekeeperLogin VALUES
                         ('Storekeeper1','thkSK01','STOREKEEPER'),
                         ('Storekeeper2','thkSK02','STOREKEEPER');

SELECT  * FROM StorekeeperLogin;
DELETE FROM StorekeeperLogin;


/*-----------------------------------Test Hashing -------------------------------------------*/

INSERT INTO StorekeeperLogin VALUES
('Storekeeper1',md5('thkSK01'),'STOREKEEPER');

INSERT INTO StorekeeperLogin VALUES ('Storekeeper2',md5('thkSK02'),'STOREKEEPER');
INSERT INTO StorekeeperLogin VALUES ('Storekeeper3',sha1('thkSK03'),'STOREKEEPER');
INSERT INTO StorekeeperLogin VALUES ('Storekeeper4',sha2('thkSK04'),'STOREKEEPER');

INSERT INTO StorekeeperLogin VALUES ('Storekeeper4',AES_ENCRYPT('thkSK04','ims'),'STOREKEEPER');
INSERT INTO StorekeeperLogin VALUES ('Storekeeper5',AES_ENCRYPT('thkSK05','ims'),'STOREKEEPER');
SELECT AES_DECRYPT('thkSK05','ims') FROM StorekeeperLogin;

/**
    1.select hex(aes_encrypt(file,'key'));
    2.select aes_decrypt(unhex(file),'key');
  */

select hex(aes_encrypt('thkWard04','ims')) from StorekeeperLogin;
select AES_DECRYPT(unhex(hex(aes_encrypt('thkWard04','ims'))),'ims') from StorekeeperLogin;
select userName,AES_DECRYPT(unhex(('thkWard04','ims')),'ims') from StorekeeperLogin;

#INSERT INTO StorekeeperLogin VALUES ('Storekeeper4',ENCRYPT('thkSK04'),'STOREKEEPER'); // deprecated

UPDATE StorekeeperLogin SET userPassword = SHA1('thkSK01') WHERE userPassword = 'thkSK01';
UPDATE StorekeeperLogin SET userPassword = SHA1('thkSK02') WHERE userPassword = 'thkSK02';


#-----------------------------------5--------------------------------

INSERT INTO Category VALUES ('CTG-001','Surgical Items'),
                            ('CTG-002','Linen'),
                            ('CTG-003','Wooden Furniture'),
                            ('CTG-004','Metal Furniture'),
                            ('CTG-005','Trays');

SELECT * FROM Category;

#---------------------------------7----------------------------------

INSERT INTO Inventory VALUES ('INV-0001','Cardiac Monitor',20,'CTG-001','L-001',10),
                             ('INV-0002','Syringe Pump',15,'CTG-001','L-001',10),
                             ('INV-0003','Glucometer',100,'CTG-001','L-001',20),
                             ('INV-0004','Cupboard',80,'CTG-003','L-002',15),
                             ('INV-0005','Bench',70,'CTG-003','L-002',10),
                             ('INV-0006','Cubical Curtain',150,'CTG-002','L-004',50),
                             ('INV-0007','GS Towel',180,'CTG-002','L-004',50),
                             ('INV-0008','Locker',75,'CTG-004','L-003',15),
                             ('INV-0009','Kidney Tray',95,'CTG-001','L-005',15),
                             ('INV-0010','Square Tray',90,'CTG-001','L-005',15);

SELECT * FROM Inventory;
DELETE FROM Inventory;

#--------------------------------------8-----------------------------
INSERT INTO InventoryWithWard VALUES

                        ('WD-001','INV-0001',10,12,'2021-03-15','Section-01',5),
                        ('WD-001','INV-0002',10,15,'2021-03-15','-',5),
                        ('WD-001','INV-0004',4,6,'2021-03-15','-',3),

                        ('WD-001','INV-0007',20,30,'2021-05-11','-',20),
                        ('WD-001','INV-0008',10,12,'2021-05-11','-',4),

                        ('WD-001','INV-0003',10,8,'2021-01-06','-',5),
                        ('WD-001','INV-0005',10,4,'2021-01-06','-',4),

                        ('WD-001','INV-0006',10,6,'2021-02-08','Section-02',10),
                        ('WD-001','INV-0009',15,13,'2021-02-08','-',8),

                        ('WD-002','INV-0002',10,7,'2021-03-10','Section-01',2),
                        ('WD-002','INV-0007',20,25,'2021-05-20','Section-01',20),
                        ('WD-002','INV-0009',10,6,'2021-03-10','-',5),

                        ('WD-003','INV-0004',10,9,'2021-04-12','-',3),
                        ('WD-003','INV-0006',15,12,'2021-04-12','-',10),
                        ('WD-003','INV-0008',10,5,'2021-04-12','-',3),

                        ('WD-004','INV-0003',6,4,'2021-05-14','-',5),
                        ('WD-004','INV-0007',11,8,'2021-05-14','Section-04',20);



                       /* ('WD-001','INV-0010',10,6,'2020-03-21','-');*/

SELECT * FROM InventoryWithWard;
DELETE FROM InventoryWithWard;

#------------------------------------------9-------------------------

INSERT INTO Condemnation VALUES
                                ('CDM-0001','WD-001','2021-01-06'),
                                ('CDM-0002','WD-001','2021-02-08'),
                                ('CDM-0003','WD-002','2021-03-10'),

                                ('CDM-0004','WD-003','2021-04-12'),
                                ('CDM-0005','WD-004','2021-05-14');

SELECT * FROM Condemnation;
DELETE FROM Condemnation;

#-------------------------------------------10------------------------

INSERT INTO CondemnDetail VALUES
                                 ('CDM-0001','INV-0003',2),
                                 ('CDM-0001','INV-0005',6),

                                 ('CDM-0002','INV-0006',4),
                                 ('CDM-0002','INV-0009',2),

                                 ('CDM-0003','INV-0002',3),
                                 ('CDM-0003','INV-0009',4),

                                 ('CDM-0004','INV-0004',1),
                                 ('CDM-0004','INV-0006',3),
                                 ('CDM-0004','INV-0008',5),

                                 ('CDM-0005','INV-0003',2),
                                 ('CDM-0005','INV-0007',3);

SELECT * FROM CondemnDetail;
DELETE FROM CondemnDetail;

#--------------------------------------------11-----------------------

INSERT INTO CondemnedInventory VALUES
                                    ('INV-0002','Syringe Pump',3),
                                    ('INV-0003','Glucometer',4),
                                    ('INV-0004','Cupboard ',1),
                                    ('INV-0005','Bench ',6),
                                    ('INV-0006','Cubical Curtain ',7),
                                    ('INV-0007','GS Towel ',3),
                                    ('INV-0008','Locker ',5),
                                    ('INV-0009','Kidney Tray',6);


SELECT * FROM CondemnedInventory;
DELETE FROM CondemnedInventory;

#------------------------------------------------12-------------------

INSERT INTO Orders VALUES
                        ('ORD-0001','2021-03-15','WD-001'),
                        ('ORD-0002','2021-05-11','WD-001'),
                        ('ORD-0003','2021-05-20','WD-002');
SELECT * FROM Orders;
DELETE FROM Orders;

#-----------------------------------13--------------------------------

INSERT INTO OrderDetail VALUES
                            ('ORD-0001','INV-0001',2),
                            ('ORD-0001','INV-0002',5),
                            ('ORD-0001','INV-0004',2),
                            ('ORD-0002','INV-0007',10),
                            ('ORD-0002','INV-0008',2),

                            ('ORD-0003','INV-0007',5);

                            /*('O-0004','INV-0007',5),
                            ('O-0003','INV-0007',5),
                            ('O-0003','INV-0007',5);*/

SELECT * FROM OrderDetail;
DELETE FROM OrderDetail;

#-----------------------------------6--------------------------------

INSERT INTO Location VALUES
                             ('L-001','Section-01','Store'),
                             ('L-002','Section-02','Store'),
                             ('L-003','Section-03','Store'),
                             ('L-004','Section-04','Store'),
                             ('L-005','Section-05','Store'),

                             ('L-006','Cupboard-01','WD-001'),
                             ('L-007','Cupboard-02','WD-001'),
                             ('L-008','Locker-01','WD-001'),
                             ('L-009','Cupboard-01','WD-002'),
                             ('L-010','Locker-01','WD-002');

                             /*('L-011','Cupboard-03','WD-001');*/

SELECT * FROM Location;
DELETE FROM Location;

#-------------------------------------------------------------------

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
ORDER BY i.invNo;

#-------------------------------------------------------------------

SELECT i.invNo, COUNT(i.locationId), l.locationName
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
group by i.locationId;

#-------------------------------------------------------------------

SELECT categoryId FROM Category WHERE categoryTitle = 'Linen';

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
WHERE i.categoryId = 'CTG-002'
order by i.invNo;

#--------------------------------------------------------------------

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
INNER JOIN Category c
ON i.categoryId = c.categoryId
ORDER BY i.invNo;

#-----------------------Filter by Category---------------------------------------------

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE c.categoryTitle = 'Surgical Items'
ORDER BY i.invNo;

#-----------------------Filter by Description---------------------------------------------

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE i.description = 'Cupboard'
ORDER BY i.invNo;

#-----------------------Filter by Location---------------------------------------------

SELECT i.invNo, i.description, i.qtyOnHand, l.locationName, c.categoryTitle
FROM Inventory i INNER JOIN Location l
ON i.locationId = l.locationId
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE l.locationName = 'Section-01'
ORDER BY i.invNo;

#-----------------------Edit Inventory Details---------------------------------------------


#-----------------------Ward No List---------------------------------------------

SELECT wardNo FROM Ward;

#-----------------------All Ward Inventory (Table)---------------------------------------------

SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified
FROM InventoryWithWard w INNER JOIN Inventory i
ON w.invNo = i.invNo
INNER JOIN Category c
on i.categoryId = c.categoryId;

#-----------------------Search - All Ward Inventory (Table)---------------------------------------------

SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified
FROM InventoryWithWard w INNER JOIN Inventory i
ON w.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE i.description LIKE '?';

#-----------------------WardWise Inventory Filter by Category(Table)---------------------------------------------

SELECT w.wardNo, w.invNo, i.description, w.afterQty, c.categoryTitle, w.lastModified
FROM InventoryWithWard w INNER JOIN Inventory i
ON w.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE c.categoryTitle = 'Linen';

#-----------------------Inventory Of Ward---------------------------------------------

SELECT i.invNo, i.description, w.afterQty, w.location
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
WHERE w.wardNo = 'WD-001'
ORDER BY i.invNo;

#-----------------------Inventory Of Ward CategoryWise---------------------------------------------

SELECT i.invNo, i.description, w.afterQty, w.location
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
AND c.categoryTitle = 'Linen'
ORDER BY i.invNo;

#-----------------------Inventory Of Ward with before and after qty---------------------------------------------

SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
ORDER BY i.invNo;


#-----------------------Update Location In Ward---------------------------------------------

UPDATE InventoryWithWard SET location = ? WHERE invNo = ?;

#-----------------------Filter Inventory In Ward by Category---------------------------------------------

SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
AND c.categoryTitle = 'Linen'
ORDER BY i.invNo;


#-----------------------Filter Inventory In Ward by Inventory---------------------------------------------

SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
AND i.invNo = 'INV-0001'
ORDER BY i.invNo;

#-----------------------Filter Inventory In Ward by Location---------------------------------------------

SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
AND w.location = 'Section-01'
ORDER BY i.invNo;

#-----------------------Search -  Ward Inventory (Table)---------------------------------------------

SELECT i.invNo, i.description, w.beforeQty, w.lastModified, w.afterQty, w.location, c.categoryTitle
FROM Inventory i INNER JOIN InventoryWithWard w
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001'
AND i.description LIKE '?';

#-----------------------Filter Combo Boxes InventoryNo and Description By Category Selected------------------

SELECT i.invNo
FROM Inventory i INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE c.categoryTitle = 'Surgical Items';

SELECT i.description
FROM Inventory i INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE c.categoryTitle = 'Surgical Items';

#--------------------------------------------------------Incharge Email-----------------------------------

SELECT email FROM InchargeLogin WHERE wardNo = 'WD-001';


#--------------------------------------------------------Before Qty / After Qty-----------------------------------

SELECT w.invNo, i.description, w.beforeQty, w. afterQty, w.lastModified, c.categoryTitle
From InventoryWithWard w INNER JOIN Inventory i
ON i.invNo = w.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE w.wardNo = 'WD-001';


SELECT o.wardNo, od.invNo, od.orderQTY, o.orderDate
FROM OrderDetail od INNER JOIN Orders o
ON od.orderId = o.orderId
WHERE o.wardNo = 'WD-001' AND o.orderDate = '2021-03-15' AND od.invNo = 'INV-0001';


SELECT c.wardNo, cd.invNo, cd.condemnQTY, c.condemnDate
FROM CondemnDetail cd INNER JOIN Condemnation c
ON cd.condemnId = c.condemnId
WHERE c.wardNo = 'WD-001' AND c.condemnDate = '2021-01-06';

/*---------------------------------------------QtyWithWard On Filter-----------------------------------------------*/

SELECT afterQty FROM InventoryWithWard
WHERE wardNo = 'WD-001' AND invNo = 'INV-0002';

/*--------------------------------------Ward OrderDetails-----------------------------------------------*/

SELECT o.orderId, o.wardNo, od.invNo, i.description, od.orderQty, o.orderDate
FROM Orders o INNER JOIN OrderDetail od
ON o.orderId = od.orderId
INNER JOIN Inventory i
ON od.invNo = i.invNo;

/*-------------------------------------------------Ward Title--------------------------------------------------*/

SELECT title FROM Ward WHERE wardNo = 'WD-001';

/*--------------------------------------Ward CondemnDetails-----------------------------------------------*/

SELECT c.condemnId, c.wardNo, cd.invNo, i.description, cd.condemnQTY, c.condemnDate
FROM Condemnation c INNER JOIN CondemnDetail cd
ON c.condemnId = cd.condemnId
INNER JOIN Inventory i
ON cd.invNo = i.invNo;

/*---------------------------------------------14---------------------------------------------------------*/

DROP TABLE IF EXISTS InchargeHistory;
CREATE TABLE IF NOT EXISTS InchargeHistory(
    wardNo VARCHAR(8) NOT NULL,
    inchargeId VARCHAR(8) NOT NULL,
    dateFrom DATE,
    dateTo DATE,

    CONSTRAINT PRIMARY KEY (wardNo,inchargeId),
    CONSTRAINT FOREIGN KEY (wardNo) REFERENCES Ward(wardNo) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (inchargeId) REFERENCES WardIncharge(inchargeId) ON DELETE CASCADE ON UPDATE CASCADE
);
SHOW TABLES;
DESCRIBE InchargeHistory;


INSERT INTO InchargeHistory (wardNo, inchargeId, dateFrom) VALUES
                                   ('WD-001','ING-001','2016-08-02'),
                                   ('WD-002','ING-002','2017-09-04'),
                                   ('WD-003','ING-003','2018-10-06'),
                                   ('WD-004','ING-004','2019-11-08');

SELECT * FROM InchargeHistory;
DELETE FROM InchargeHistory;
/*--------------------------------------------Incharges that are assigned to a ward but not yet signed up--------------*/

SELECT w.wardNo, w.inchargeId, inc.wardNo
FROM Ward w LEFT JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo
WHERE inc.wardNo IS NULL;

SELECT w.wardNo, wi.inchargeId, inc.wardNo
FROM Ward w LEFT JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo
RIGHT JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
WHERE inc.wardNo IS NULL;

/*----------------------------------------------Ward Incharge Details-----------------------------*/

SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName
FROM Ward w INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
INNER JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo;

SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId
FROM Ward w INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
LEFT JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo;

SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId
FROM Ward w INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
LEFT JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo
WHERE title = 'Medical Ward';

/*-------------------------------------------------Search Ward Details-----------------------------------------------*/

SELECT w.wardNo, w.title, w.extensionNo, inc.email, wi.inchargeName, wi.inchargeId
FROM Ward w INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
LEFT JOIN InchargeLogin inc
ON w.wardNo = inc.wardNo
WHERE w.title LIKE '%m%'
OR wi.inchargeName LIKE '%m%';

/*--------------------------------------------------------------------------------------------------*/

SELECT title, COUNT(wardNo) FROM Ward GROUP BY title;

/*---------------------------------------------Storekeeper Dashboard Labels-----------------------------------------------------*/

#-----------------------------------Surgical Items---------------------------
/*SELECT COUNT(i.invNo)
FROM Inventory i INNER JOIN  Category c
ON i.categoryId = c.categoryId
WHERE c.categoryId = 'CTG-001'
GROUP BY i.categoryId;*/

SELECT SUM(i.qtyOnHand)
FROM Inventory i INNER JOIN  Category c
ON i.categoryId = c.categoryId
WHERE c.categoryId = 'CTG-001'
GROUP BY i.categoryId;

/*-----------------------------------------------Values for BarChart--------------------------*/
SELECT c.categoryTitle, SUM(i.qtyOnHand)
FROM Inventory i INNER JOIN  Category c
ON i.categoryId = c.categoryId
GROUP BY i.categoryId;

SELECT c.categoryTitle, SUM(iw.afterQty)
FROM InventoryWithWard iw INNER JOIN Inventory i
ON iw.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE iw.wardNo = 'WD-001'
GROUP BY c.categoryId;

#-----------------------------------------------------------------------------
SELECT COUNT(c.categoryId) FROM Category c;

/*SELECT COUNT(invNo) FROM Inventory;*/
SELECT SUM(qtyOnHand) FROM Inventory;

SELECT COUNT(wardNo) FROM Ward;

SELECT COUNT(orderId) FROM Orders WHERE orderDate = '2021-05-20';

SELECT COUNT(condemnId) FROM Condemnation WHERE condemnDate = '2021-05-14';

/*---------------------------------------------Incharge Dashboard Labels-----------------------------------------------------*/

#------------------total Inventory-------------------
SELECT wardNo, COUNT(invNo)
FROM InventoryWithWard
WHERE wardNo = 'WD-001';

#-----------------Surgical Items-------------------------

/*SELECT COUNT(c.categoryId),c.categoryTitle
FROM InventoryWithWard iw INNER JOIN Inventory i
ON iw.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE iw.wardNo = 'WD-003'
AND c.categoryId = 'CTG-001'
GROUP BY c.categoryId;*/

SELECT SUM(iw.afterQty)
FROM InventoryWithWard iw INNER JOIN Inventory i
ON iw.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE iw.wardNo = 'WD-001'
AND c.categoryId = 'CTG-002'
GROUP BY c.categoryId;

#------------------------------Category Count----------------------------
SELECT COUNT(DISTINCT c.categoryId)
FROM InventoryWithWard iw INNER JOIN Inventory i
ON iw.invNo = i.invNo
INNER JOIN Category c
ON i.categoryId = c.categoryId
WHERE iw.wardNo = 'WD-001';

#---------------------------------Total Inventory Count------------------------
SELECT COUNT(invNo)
FROM InventoryWithWard
WHERE wardNo = 'WD-001';

#-----------------------------Location Count Ward wise--------------------------------

SELECT COUNT(locationId) from Location where createdBy = 'WD-001';

/*--------------------------Monthly Order Count of Ward-------------------------------*/

SELECT COUNT(orderId)
FROM Orders
WHERE wardNo = 'WD-003'
AND orderDate LIKE '%-9-%';

/*--------------------------Monthly Condemn Count of Ward-------------------------------*/

SELECT COUNT(condemnId)
FROM Condemnation
WHERE wardNo = 'WD-001'
AND condemnDate LIKE '%-02-%';

/*---------------------------------Inventory Below Level (10) Count-Restock-------------------------*/

SELECT COUNT(invNo)
FROM InventoryWithWard
WHERE wardNo = 'WD-002'
AND afterQty < 10;

/*------------------------------------------Jasper Report-----------------------------------------*/

SELECT c.categoryId, c.categoryTitle, i.invNo, i.description, iw.afterQty,wi.inchargeId,wi.inchargeName
FROM Inventory i INNER JOIN InventoryWithWard iw
ON i.invNo = iw.invNo
INNER JOIN Category c
ON i.categoryId =c.categoryId
INNER JOIN InchargeLogin inc
ON iw.wardNo = inc.wardNo
INNER JOIN Ward w
ON inc.wardNo = w.wardNo
INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
WHERE iw.wardNo = 'WD-001';

/*---------------------------------Setting Parameters---------------------------------------------*/

SELECT wi.inchargeId, wi.inchargeName
FROM WardIncharge wi INNER JOIN Ward w
ON wi.inchargeId = w.inchargeId
WHERE w.wardNo = 'WD-001'
ORDER BY wi.inchargeId DESC LIMIT 1;

SELECT dateFrom FROM InchargeHistory WHERE inchargeId = 'ING-001';

/*SELECT orderId, orderDate
FROM Orders o INNER JOIN Ward w
ON o.wardNo = w.wardNo
INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
INNER JOIN InchargeHistory ih
ON wi.inchargeId = ih.inchargeId
WHERE w.wardNo = 'WD-001'
AND wi.inchargeId = 'ING-001'
AND orderDate BETWEEN '2021-03-15' AND '2021-09-28';*/


SELECT orderId, orderDate
FROM Orders
WHERE orderDate BETWEEN '2021-03-15' AND '2021-09-28'
AND wardNo = 'WD-003';


SELECT COUNT(orderId)
FROM Orders
WHERE orderDate BETWEEN '2021-03-15' AND '2021-09-28'
AND wardNo = 'WD-003';


SELECT condemnId, condemnDate
FROM Condemnation
WHERE condemnDate BETWEEN '2021-03-15' AND '2021-09-28'
AND wardNo = 'WD-001';

SELECT COUNT(condemnId)
FROM Condemnation
WHERE condemnDate BETWEEN '2021-03-15' AND '2021-09-28'
AND wardNo = 'WD-001';

/*SELECT c.condemnId, c.condemnDate
FROM Condemnation c INNER JOIN Ward w
ON c.wardNo = w.wardNo
INNER JOIN WardIncharge wi
ON w.inchargeId = wi.inchargeId
WHERE w.wardNo = 'WD-001'
AND wi.inchargeId = 'ING-001';*/


/*--------------------------------------------------------------------------------------------------*/

update InchargeLogin set userName = 'Incharge1', userPassword = 'thkWard01' where wardNo = 'WD-001';
update Ward set inchargeId = 'ING-001' where wardNo = 'WD-001';


Update InchargeHistory set dateFrom = '2021-03-15' where inchargeId = 'ING-001';
Update InchargeHistory set dateFrom = '2021-05-20' where inchargeId = 'ING-002';
Update InchargeHistory set dateFrom = '2021-08-06' where inchargeId = 'ING-003';
Update InchargeHistory set dateFrom = '2021-08-08' where inchargeId = 'ING-004';


/*--------------------------------------------------------------------------------------------------*/


SELECT * FROM InventoryWithWard
WHERE wardNo = 'WD-001'
AND afterQty < notifyLevel;

SELECT COUNT(invNo) FROM InventoryWithWard
WHERE wardNo = 'WD-002'
AND afterQty < notifyLevel;


SELECT COUNT(invNo) FROM Inventory
WHERE qtyOnHand < notifyLevel;


