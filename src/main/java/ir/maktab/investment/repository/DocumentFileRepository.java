package ir.maktab.investment.repository;

import ir.maktab.investment.model.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFileRepository extends JpaRepository<DocumentFile,Long> {

//    List<DocumentFile> findAllOrderByUploadDateDesc();
}
