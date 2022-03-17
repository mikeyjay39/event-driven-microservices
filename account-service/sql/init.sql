--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4 (Debian 12.4-1.pgdg100+1)
-- Dumped by pg_dump version 14.2 (Ubuntu 14.2-1.pgdg20.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

-- CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8';


ALTER DATABASE postgres OWNER TO postgres;
CREATE DATABASE tenant1;
ALTER DATABASE tenant1 OWNER TO postgres;


\connect tenant1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: association_value_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.association_value_entry (
    id bigint NOT NULL,
    association_key character varying(255) NOT NULL,
    association_value character varying(255),
    saga_id character varying(255) NOT NULL,
    saga_type character varying(255)
);


ALTER TABLE public.association_value_entry OWNER TO postgres;

--
-- Name: domain_event_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.domain_event_entry (
    global_index bigint NOT NULL,
    event_identifier character varying(255) NOT NULL,
    meta_data oid,
    payload oid NOT NULL,
    payload_revision character varying(255),
    payload_type character varying(255) NOT NULL,
    time_stamp character varying(255) NOT NULL,
    aggregate_identifier character varying(255) NOT NULL,
    sequence_number bigint NOT NULL,
    type character varying(255)
);


ALTER TABLE public.domain_event_entry OWNER TO postgres;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- Name: saga_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.saga_entry (
    saga_id character varying(255) NOT NULL,
    revision character varying(255),
    saga_type character varying(255),
    serialized_saga oid
);


ALTER TABLE public.saga_entry OWNER TO postgres;

--
-- Name: snapshot_event_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.snapshot_event_entry (
    aggregate_identifier character varying(255) NOT NULL,
    sequence_number bigint NOT NULL,
    type character varying(255) NOT NULL,
    event_identifier character varying(255) NOT NULL,
    meta_data oid,
    payload oid NOT NULL,
    payload_revision character varying(255),
    payload_type character varying(255) NOT NULL,
    time_stamp character varying(255) NOT NULL
);


ALTER TABLE public.snapshot_event_entry OWNER TO postgres;

--
-- Name: token_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token_entry (
    processor_name character varying(255) NOT NULL,
    segment integer NOT NULL,
    owner character varying(255),
    "timestamp" character varying(255) NOT NULL,
    token oid,
    token_type character varying(255)
);


ALTER TABLE public.token_entry OWNER TO postgres;

--
-- Data for Name: association_value_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: domain_event_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: saga_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: snapshot_event_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: token_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- Name: association_value_entry association_value_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.association_value_entry
    ADD CONSTRAINT association_value_entry_pkey PRIMARY KEY (id);


--
-- Name: domain_event_entry domain_event_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT domain_event_entry_pkey PRIMARY KEY (global_index);


--
-- Name: saga_entry saga_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.saga_entry
    ADD CONSTRAINT saga_entry_pkey PRIMARY KEY (saga_id);


--
-- Name: snapshot_event_entry snapshot_event_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.snapshot_event_entry
    ADD CONSTRAINT snapshot_event_entry_pkey PRIMARY KEY (aggregate_identifier, sequence_number, type);


--
-- Name: token_entry token_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_entry
    ADD CONSTRAINT token_entry_pkey PRIMARY KEY (processor_name, segment);


--
-- Name: domain_event_entry uk8s1f994p4la2ipb13me2xqm1w; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT uk8s1f994p4la2ipb13me2xqm1w UNIQUE (aggregate_identifier, sequence_number);


--
-- Name: snapshot_event_entry uk_e1uucjseo68gopmnd0vgdl44h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.snapshot_event_entry
    ADD CONSTRAINT uk_e1uucjseo68gopmnd0vgdl44h UNIQUE (event_identifier);


--
-- Name: domain_event_entry uk_fwe6lsa8bfo6hyas6ud3m8c7x; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT uk_fwe6lsa8bfo6hyas6ud3m8c7x UNIQUE (event_identifier);


--
-- Name: idxgv5k1v2mh6frxuy5c0hgbau94; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxgv5k1v2mh6frxuy5c0hgbau94 ON public.association_value_entry USING btree (saga_id, saga_type);


--
-- Name: idxk45eqnxkgd8hpdn6xixn8sgft; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxk45eqnxkgd8hpdn6xixn8sgft ON public.association_value_entry USING btree (saga_type, association_key, association_value);

create table public.account
(
    account_number varchar(255),
    balance        numeric(19, 2),
    user_id        varchar(255)
);

alter table public.account
    owner to postgres;


--
-- PostgreSQL database dump complete
--

--- Init second tenant database
CREATE DATABASE tenant2 owner postgres;
ALTER DATABASE tenant2 OWNER TO postgres;

\connect tenant2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

-- CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8';



--\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: DATABASE postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: association_value_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.association_value_entry (
                                                id bigint NOT NULL,
                                                association_key character varying(255) NOT NULL,
                                                association_value character varying(255),
                                                saga_id character varying(255) NOT NULL,
                                                saga_type character varying(255)
);


ALTER TABLE public.association_value_entry OWNER TO postgres;

--
-- Name: domain_event_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.domain_event_entry (
                                           global_index bigint NOT NULL,
                                           event_identifier character varying(255) NOT NULL,
                                           meta_data oid,
                                           payload oid NOT NULL,
                                           payload_revision character varying(255),
                                           payload_type character varying(255) NOT NULL,
                                           time_stamp character varying(255) NOT NULL,
                                           aggregate_identifier character varying(255) NOT NULL,
                                           sequence_number bigint NOT NULL,
                                           type character varying(255)
);


ALTER TABLE public.domain_event_entry OWNER TO postgres;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- Name: saga_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.saga_entry (
                                   saga_id character varying(255) NOT NULL,
                                   revision character varying(255),
                                   saga_type character varying(255),
                                   serialized_saga oid
);


ALTER TABLE public.saga_entry OWNER TO postgres;

--
-- Name: snapshot_event_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.snapshot_event_entry (
                                             aggregate_identifier character varying(255) NOT NULL,
                                             sequence_number bigint NOT NULL,
                                             type character varying(255) NOT NULL,
                                             event_identifier character varying(255) NOT NULL,
                                             meta_data oid,
                                             payload oid NOT NULL,
                                             payload_revision character varying(255),
                                             payload_type character varying(255) NOT NULL,
                                             time_stamp character varying(255) NOT NULL
);


ALTER TABLE public.snapshot_event_entry OWNER TO postgres;

--
-- Name: token_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token_entry (
                                    processor_name character varying(255) NOT NULL,
                                    segment integer NOT NULL,
                                    owner character varying(255),
                                    "timestamp" character varying(255) NOT NULL,
                                    token oid,
                                    token_type character varying(255)
);


ALTER TABLE public.token_entry OWNER TO postgres;

--
-- Data for Name: association_value_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: domain_event_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: saga_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: snapshot_event_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: token_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- Name: association_value_entry association_value_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.association_value_entry
    ADD CONSTRAINT association_value_entry_pkey PRIMARY KEY (id);


--
-- Name: domain_event_entry domain_event_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT domain_event_entry_pkey PRIMARY KEY (global_index);


--
-- Name: saga_entry saga_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.saga_entry
    ADD CONSTRAINT saga_entry_pkey PRIMARY KEY (saga_id);


--
-- Name: snapshot_event_entry snapshot_event_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.snapshot_event_entry
    ADD CONSTRAINT snapshot_event_entry_pkey PRIMARY KEY (aggregate_identifier, sequence_number, type);


--
-- Name: token_entry token_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_entry
    ADD CONSTRAINT token_entry_pkey PRIMARY KEY (processor_name, segment);


--
-- Name: domain_event_entry uk8s1f994p4la2ipb13me2xqm1w; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT uk8s1f994p4la2ipb13me2xqm1w UNIQUE (aggregate_identifier, sequence_number);


--
-- Name: snapshot_event_entry uk_e1uucjseo68gopmnd0vgdl44h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.snapshot_event_entry
    ADD CONSTRAINT uk_e1uucjseo68gopmnd0vgdl44h UNIQUE (event_identifier);


--
-- Name: domain_event_entry uk_fwe6lsa8bfo6hyas6ud3m8c7x; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.domain_event_entry
    ADD CONSTRAINT uk_fwe6lsa8bfo6hyas6ud3m8c7x UNIQUE (event_identifier);


--
-- Name: idxgv5k1v2mh6frxuy5c0hgbau94; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxgv5k1v2mh6frxuy5c0hgbau94 ON public.association_value_entry USING btree (saga_id, saga_type);


--
-- Name: idxk45eqnxkgd8hpdn6xixn8sgft; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idxk45eqnxkgd8hpdn6xixn8sgft ON public.association_value_entry USING btree (saga_type, association_key, association_value);

create table public.app_user
(
    id          bigserial
        constraint app_user_pk
            primary key,
    "user_name"  varchar(255),
    "first_name" varchar(255),
    "last_name"  varchar(255)
);

alter table public.app_user
    owner to postgres;

create unique index app_user_username_uindex
    on public.app_user ("user_name");


--
-- PostgreSQL database dump complete
--
