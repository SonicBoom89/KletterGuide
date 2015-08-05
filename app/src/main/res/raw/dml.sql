BEGIN TRANSACTION;
INSERT INTO "CLIMBINGAREAS" VALUES(1,'Riederin Bodenmais',48.574326,13.404525, 4.5, 'OUTDOOR', 'riederin.jpg', 'Für Kletterfans ist in der Umgebung von Bodenmais wirklich einiges Geboten. Der beste Tipp ist der von Bodenmais 10 Minuten entfernte Kaitersberg. Mit über 100 Routen findet dort jeder die passende Wand für sich. Infos und Topologien Kaitersberg. In Bodenmais selbst sind in den letzten Jahren auch 2 Klettergebiete entstanden. Am Wunderschönen Riederin - Felsen findet man ca. 20 Routen vor und am Silberberg in Bodenmais gibt es auch noch viele Routen zu finden. Wem das nicht reicht findet in Rabenstein ca. 15 Minuten mit dem Auto von Bodenmais entfernt auch nochmal mehrere gute Kletterfelsen, die sich bestens zum Klettern und Bouldern eignen.');
INSERT INTO "CLIMBINGAREAS" VALUES(2,'Saulock Deggendorf',48.774326,13.304525, 4.0, 'OUTDOOR', null, null);
INSERT INTO "ROUTES" VALUES(1, 1, 'Kamin', 3.0, "3",48.574326,13.404525, null, 1, 0, 1);
INSERT INTO "ROUTES" VALUES(2, 1, 'Sauschwaar', 3.0, "7+",48.574326,13.404525, null, 1, 1, 1);
INSERT INTO "ROUTES" VALUES(3, 2, 'Grube', 4.5, "6-",48.774326,13.304525, null, 1, 0, 1);
INSERT INTO "ROUTES" VALUES(4, 2, 'Aussicht', 3.5, "7", 48.574326,13.404525, null, 1, 0, 0);
COMMIT;
