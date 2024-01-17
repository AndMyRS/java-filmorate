DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS "films" (
  "id" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(255) UNIQUE NOT NULL,
  "description" varchar(350),
  "release_date" timestamp,
  "duration" integer,
  "rating_id" integer
);

CREATE TABLE IF NOT EXISTS "users" (
  "id" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "email" varchar(255) UNIQUE NOT NULL,
  "login" varchar(255) UNIQUE NOT NULL,
  "name" varchar(255) NOT NULL,
  "birthday" timestamp
);

CREATE TABLE IF NOT EXISTS "ratings" (
  "id" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(100)
);

CREATE TABLE IF NOT EXISTS "film_likes" (
  "film_id" integer,
  "user_id" integer,
  PRIMARY KEY ("film_id", "user_id")
);

CREATE TABLE IF NOT EXISTS "genres" (
  "id" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(50)
);

CREATE TABLE IF NOT EXISTS "film_genres" (
  "film_id" integer,
  "genre_id" integer,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE IF NOT EXISTS "friends" (
  "user_id" integer,
  "friend_id" integer,
  PRIMARY KEY ("user_id", "friend_id")
);

ALTER TABLE "film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "films" ADD FOREIGN KEY ("rating_id") REFERENCES "ratings" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");