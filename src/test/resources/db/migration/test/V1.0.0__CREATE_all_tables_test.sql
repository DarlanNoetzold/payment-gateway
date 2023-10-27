-- Table: public.boletomodel

-- DROP TABLE IF EXISTS public.boletomodel;

CREATE TABLE IF NOT EXISTS public.boletomodel
(
    jurosatraso double precision NOT NULL,
    multa double precision NOT NULL,
    valor double precision NOT NULL,
    dataemissao timestamp(6) without time zone,
    datavencimento timestamp(6) without time zone,
    boletoid uuid NOT NULL,
    beneficiariocnpjcpf character varying(255) COLLATE pg_catalog."default",
    beneficiarioendereco character varying(255) COLLATE pg_catalog."default",
    beneficiarionome character varying(255) COLLATE pg_catalog."default",
    codigoautenticacao character varying(255) COLLATE pg_catalog."default",
    codigobanco character varying(255) COLLATE pg_catalog."default",
    codigodebarras character varying(255) COLLATE pg_catalog."default",
    identificacaoboleto character varying(255) COLLATE pg_catalog."default",
    informacoescontato character varying(255) COLLATE pg_catalog."default",
    instrucoespagamento character varying(255) COLLATE pg_catalog."default",
    linhadigitavel character varying(255) COLLATE pg_catalog."default",
    localpagamento character varying(255) COLLATE pg_catalog."default",
    logobanco character varying(255) COLLATE pg_catalog."default",
    numerodocumento character varying(255) COLLATE pg_catalog."default",
    sacadocnpjcpf character varying(255) COLLATE pg_catalog."default",
    sacadoendereco character varying(255) COLLATE pg_catalog."default",
    sacadonome character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT boletomodel_pkey PRIMARY KEY (boletoid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.boletomodel
    OWNER to postgres;



-- Table: public.customermodel

-- DROP TABLE IF EXISTS public.customermodel;

CREATE TABLE IF NOT EXISTS public.customermodel
(
    borndate timestamp(6) without time zone,
    registerdate timestamp(6) without time zone,
    customerid uuid NOT NULL,
    userid character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customermodel_pkey PRIMARY KEY (customerid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.customermodel
    OWNER to postgres;


-- Table: public.cardmodel

-- DROP TABLE IF EXISTS public.cardmodel;

CREATE TABLE IF NOT EXISTS public.cardmodel
(
    cardtype smallint,
    cardid uuid NOT NULL,
    card_id uuid,
    cardholdername character varying(255) COLLATE pg_catalog."default",
    cardnumber character varying(255) COLLATE pg_catalog."default",
    cvv character varying(255) COLLATE pg_catalog."default",
    expirationdate character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cardmodel_pkey PRIMARY KEY (cardid),
    CONSTRAINT fknkg7up96qv8rxgpogn5isdsp5 FOREIGN KEY (card_id)
        REFERENCES public.customermodel (customerid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT cardmodel_cardtype_check CHECK (cardtype >= 0 AND cardtype <= 1)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cardmodel
    OWNER to postgres;

-- Table: public.paypalmodel

-- DROP TABLE IF EXISTS public.paypalmodel;

CREATE TABLE IF NOT EXISTS public.paypalmodel
(
    paypalid uuid NOT NULL,
    descricao character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    identificadortransacao character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT paypalmodel_pkey PRIMARY KEY (paypalid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.paypalmodel
    OWNER to postgres;



-- Table: public.pixmodel

-- DROP TABLE IF EXISTS public.pixmodel;

CREATE TABLE IF NOT EXISTS public.pixmodel
(
    pixid uuid NOT NULL,
    chavepix character varying(255) COLLATE pg_catalog."default",
    descricao character varying(255) COLLATE pg_catalog."default",
    identificadortransacao character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT pixmodel_pkey PRIMARY KEY (pixid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.pixmodel
    OWNER to postgres;


-- Table: public.paymentmodel

-- DROP TABLE IF EXISTS public.paymentmodel;

CREATE TABLE IF NOT EXISTS public.paymentmodel
(
    dicountamount double precision NOT NULL,
    haserrors boolean NOT NULL,
    paymentmethod smallint,
    paymentstate smallint,
    totalamount double precision NOT NULL,
    registerdate timestamp(6) without time zone,
    boleto_model_id uuid,
    card_model_id uuid,
    customer_id uuid,
    paymentid uuid NOT NULL,
    paypal_model_id uuid,
    pix_model_id uuid,
    transaction_id uuid,
    orderid character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT paymentmodel_pkey PRIMARY KEY (paymentid),
    CONSTRAINT fk190soas8n9eub343menx2nuja FOREIGN KEY (card_model_id)
        REFERENCES public.cardmodel (cardid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk5d05tisy0i6gy23yy1etfhr8v FOREIGN KEY (transaction_id)
        REFERENCES public.customermodel (customerid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkb8dwp5q7w33yd6g4sc1kwebsy FOREIGN KEY (boleto_model_id)
        REFERENCES public.boletomodel (boletoid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkeon7tnqt5s14cwbkg37d7vhw1 FOREIGN KEY (pix_model_id)
        REFERENCES public.pixmodel (pixid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkj2utlg247avf6fq4c4y5yrrfy FOREIGN KEY (paypal_model_id)
        REFERENCES public.paypalmodel (paypalid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkq6ka3wc78fbfkiib45ld474nm FOREIGN KEY (customer_id)
        REFERENCES public.customermodel (customerid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT paymentmodel_paymentmethod_check CHECK (paymentmethod >= 0 AND paymentmethod <= 4),
    CONSTRAINT paymentmodel_paymentstate_check CHECK (paymentstate >= 0 AND paymentstate <= 5)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.paymentmodel
    OWNER to postgres;


-- Table: public.invoicemodel

-- DROP TABLE IF EXISTS public.invoicemodel;

CREATE TABLE IF NOT EXISTS public.invoicemodel
(
    discountamount double precision NOT NULL,
    totalamount double precision NOT NULL,
    invoicedate timestamp(6) without time zone,
    customer_id uuid,
    invoiceid uuid NOT NULL,
    paymentid uuid,
    buyeraddress character varying(255) COLLATE pg_catalog."default",
    buyername character varying(255) COLLATE pg_catalog."default",
    invoicenumber character varying(255) COLLATE pg_catalog."default",
    selleraddress character varying(255) COLLATE pg_catalog."default",
    sellername character varying(255) COLLATE pg_catalog."default",
    itemsid character varying(255)[] COLLATE pg_catalog."default",
    CONSTRAINT invoicemodel_pkey PRIMARY KEY (invoiceid),
    CONSTRAINT fka7bs6ca85162q5ewe47xdoclf FOREIGN KEY (paymentid)
        REFERENCES public.paymentmodel (paymentid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fks5xbja22826db1nb8mp50t4dd FOREIGN KEY (customer_id)
        REFERENCES public.customermodel (customerid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.invoicemodel
    OWNER to postgres;
