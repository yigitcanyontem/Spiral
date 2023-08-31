create table public.country (
                                id integer primary key not null,
                                iso character varying(255),
                                iso3 character varying(255),
                                name character varying(255),
                                nicename character varying(255),
                                numcode integer,
                                phonecode integer
);

create table public.users (
                              id integer primary key not null,
                              date_of_birth date not null,
                              email character varying(255) not null,
                              first_name character varying(255) not null,
                              last_name character varying(255) not null,
                              password character varying(255) not null,
                              role character varying(255) not null,
                              username character varying(255) not null,
                              country integer not null,
                              status character varying(255) not null,
                              CHECK (role IN ('USER', 'ADMIN', 'MANAGER')),
                              CHECK (status IN ('ACTIVE', 'INACTIVE')),
                              foreign key (country) references public.country (id)
                                  match simple on update no action on delete no action

);

create table public.description (
                                    usersid integer primary key not null,
                                    description character varying(255)
);

create table public.favalbums (
                                  id integer primary key not null,
                                  albumid character varying(255) not null,
                                  usersid integer not null,
                                  foreign key (usersid) references public.users (id)
                                      match simple on update no action on delete no action
);

create table public.favbooks (
                                 id integer primary key not null,
                                 bookid character varying(255) not null,
                                 usersid integer not null,
                                 foreign key (usersid) references public.users (id)
                                     match simple on update no action on delete no action
);

create table public.favmovie (
                                 id integer primary key not null,
                                 movieid integer not null,
                                 usersid integer not null,
                                 foreign key (usersid) references public.users (id)
                                     match simple on update no action on delete no action
);

create table public.favshows (
                                 id integer primary key not null,
                                 showid integer not null,
                                 usersid integer not null,
                                 foreign key (usersid) references public.users (id)
                                     match simple on update no action on delete no action
);

create table public.favgames (
                                 id integer primary key not null,
                                 gameid character varying(255) not null,
                                 usersid integer not null,
                                 foreign key (usersid) references public.users (id)
                                     match simple on update no action on delete no action
);

create table public.socialmedia (
                                    id integer primary key not null,
                                    instagram character varying(255),
                                    linkedin character varying(255),
                                    pinterest character varying(255),
                                    twitter character varying(255),
                                    usersid integer not null,
                                    foreign key (usersid) references public.users (id)
                                        match simple on update no action on delete no action
);

create table public.review (
                                    id integer primary key not null,
                                    usersid integer not null,
                                    entertainmenttype character varying(255) not null,
                                    description TEXT not null,
                                    rating integer not null,
                                    upvote integer not null,
                                    downvote integer not null,
                                    entertainmentid character varying(255) not null,
                                    date date not null,
                                    CHECK (entertainmenttype IN ('MOVIE', 'SHOW', 'ALBUM', 'BOOK', 'GAME')),
                                    foreign key (usersid) references public.users (id)
                                        match simple on update no action on delete no action
);



create table public.token (
                              id integer primary key not null,
                              expired boolean not null,
                              revoked boolean not null,
                              token character varying(255),
                              token_type character varying(255),
                              user_id integer,
                              foreign key (user_id) references public.users (id)
                                  match simple on update no action on delete no action
);
create unique index uk_pddrhgwxnms2aceeku9s2ewy5 on token using btree (token);



CREATE SEQUENCE user_id_seq START 1;
create unique index user_email_unique on users using btree (email);
create unique index user_username_unique on users using btree (username);
create unique index review_user_unique on review using btree (usersid,entertainmentid,entertainmenttype);

