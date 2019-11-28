-- ----------------------------------------------------------------------------
--
-- ----------------------------------------------------------------------------
IF OBJECT_ID('dbo.input_table', 'U') IS NOT NULL
  BEGIN
    PRINT 'TRUNCATE TABLE [dbo].[input_table]';
	TRUNCATE TABLE [dbo].[input_table];
    PRINT 'ALTER INDEX ALL ON [dbo].[input_table] REBUILD';
	ALTER INDEX ALL ON [dbo].[input_table] REBUILD;
    PRINT 'DBCC CHECKIDENT ("[dbo].[input_table]", RESEED, 1)';
	DBCC CHECKIDENT ("[dbo].[input_table]", RESEED, 1);
  END;
ELSE
  BEGIN
    PRINT 'TABLE [dbo].[input_table] does *NOT* exists!';
  END;
GO
