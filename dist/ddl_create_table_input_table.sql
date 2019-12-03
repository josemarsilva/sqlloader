-- ----------------------------------------------------------------------------
--
-- ----------------------------------------------------------------------------
IF OBJECT_ID('dbo.input_table', 'U') IS NOT NULL
  BEGIN
    PRINT 'DROP TABLE [dbo].[input_table]';
	DROP TABLE [dbo].[input_table];
  END;
--
PRINT 'CREATE TABLE [dbo].[input_table]';
CREATE TABLE [dbo].[input_table]
(
	[id]    [int] IDENTITY(1,1)  NOT NULL,
	[col_1] [int]                NOT NULL,
	[col_2] [nvarchar] (30)      NOT NULL,
	[col_3] [nvarchar] (30)      NOT NULL,
	[col_4] [nvarchar] (30)      NOT NULL,
	[col_5] [nvarchar] (30)      NOT NULL,
PRIMARY KEY CLUSTERED 
	(
	  [id] ASC
	) 
);
GO
