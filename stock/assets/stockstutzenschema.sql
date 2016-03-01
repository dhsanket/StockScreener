--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4
-- Started on 2013-08-21 10:43:06

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 42960)
-- Name: stockhistory_tbl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stockhistory_tbl (
    stockhistid integer NOT NULL,
    stockhistsymbol text,
    stockhistopen real,
    stockhisthigh real,
    stockhistlow real,
    stockhistclose real,
    stockhistvolume numeric,
    stockhistadjclose real,
    stockhistdate timestamp without time zone
);


ALTER TABLE public.stockhistory_tbl OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 42958)
-- Name: stockhistory_tbl_stockhistid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE stockhistory_tbl_stockhistid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stockhistory_tbl_stockhistid_seq OWNER TO postgres;

--
-- TOC entry 1952 (class 0 OID 0)
-- Dependencies: 170
-- Name: stockhistory_tbl_stockhistid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE stockhistory_tbl_stockhistid_seq OWNED BY stockhistory_tbl.stockhistid;


--
-- TOC entry 173 (class 1259 OID 42971)
-- Name: stockinfo_tbl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stockinfo_tbl (
    stockinfoid integer NOT NULL,
    curdate text,
    volume real,
    marketcap real,
    pe real,
    price text,
    stockname text,
    stocksymbol text
);


ALTER TABLE public.stockinfo_tbl OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 42969)
-- Name: stockinfo_tbl_stockinfoid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE stockinfo_tbl_stockinfoid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stockinfo_tbl_stockinfoid_seq OWNER TO postgres;

--
-- TOC entry 1953 (class 0 OID 0)
-- Dependencies: 172
-- Name: stockinfo_tbl_stockinfoid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE stockinfo_tbl_stockinfoid_seq OWNED BY stockinfo_tbl.stockinfoid;


--
-- TOC entry 169 (class 1259 OID 42948)
-- Name: stocksymbol_tbl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE stocksymbol_tbl (
    stockid integer NOT NULL,
    stocksymbol text NOT NULL,
    stockname text,
    isactive boolean DEFAULT true,
    stockexchange text
);


ALTER TABLE public.stocksymbol_tbl OWNER TO postgres;

--
-- TOC entry 168 (class 1259 OID 42946)
-- Name: stocksymbol_tbl_stockid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE stocksymbol_tbl_stockid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.stocksymbol_tbl_stockid_seq OWNER TO postgres;

--
-- TOC entry 1954 (class 0 OID 0)
-- Dependencies: 168
-- Name: stocksymbol_tbl_stockid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE stocksymbol_tbl_stockid_seq OWNED BY stocksymbol_tbl.stockid;


--
-- TOC entry 1938 (class 2604 OID 42986)
-- Name: stockhistid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY stockhistory_tbl ALTER COLUMN stockhistid SET DEFAULT nextval('stockhistory_tbl_stockhistid_seq'::regclass);


--
-- TOC entry 1939 (class 2604 OID 42987)
-- Name: stockinfoid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY stockinfo_tbl ALTER COLUMN stockinfoid SET DEFAULT nextval('stockinfo_tbl_stockinfoid_seq'::regclass);


--
-- TOC entry 1937 (class 2604 OID 42988)
-- Name: stockid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY stocksymbol_tbl ALTER COLUMN stockid SET DEFAULT nextval('stocksymbol_tbl_stockid_seq'::regclass);


--
-- TOC entry 1943 (class 2606 OID 42968)
-- Name: stockhistory_tbl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stockhistory_tbl
    ADD CONSTRAINT stockhistory_tbl_pkey PRIMARY KEY (stockhistid);


--
-- TOC entry 1946 (class 2606 OID 42979)
-- Name: stockinfo_tbl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stockinfo_tbl
    ADD CONSTRAINT stockinfo_tbl_pkey PRIMARY KEY (stockinfoid);


--
-- TOC entry 1941 (class 2606 OID 42957)
-- Name: stocksymbol_tbl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY stocksymbol_tbl
    ADD CONSTRAINT stocksymbol_tbl_pkey PRIMARY KEY (stockid);


--
-- TOC entry 1944 (class 1259 OID 51605)
-- Name: stockhistsymbol; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX stockhistsymbol ON stockhistory_tbl USING btree (stockhistsymbol varchar_ops);


--
-- TOC entry 1947 (class 1259 OID 60133)
-- Name: stockinfo_tbl_stocksymbol_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX stockinfo_tbl_stocksymbol_idx ON stockinfo_tbl USING btree (stocksymbol);


-- Completed on 2013-08-21 10:43:07

--
-- PostgreSQL database dump complete
--

